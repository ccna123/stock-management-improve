package com.example.sol_denka_stockmanagement.screen.inventory.complete

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryCompleteRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.InventoryResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class InventoryCompleteViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val locationMasterRepository: LocationMasterRepository,
    private val inventoryResultTypeRepository: InventoryResultTypeRepository,
    private val inventoryCompleteRepository: InventoryCompleteRepository
) : ViewModel() {

    private val _wrongLocationCount = MutableStateFlow(0)
    val wrongLocationCount: StateFlow<Int> = _wrongLocationCount.asStateFlow()

    private val _okCount = MutableStateFlow(0)
    val okCount = _okCount.asStateFlow()

    private val _shortageCount = MutableStateFlow(0)
    val shortageCount = _shortageCount.asStateFlow()

    private val _overCount = MutableStateFlow(0)
    val overCount = _overCount.asStateFlow()

    private val csvModels = mutableListOf<InventoryResultCsvModel>()

    private val _finalTagList = MutableStateFlow<List<TagMasterModel>>(emptyList())


    fun computeResult(rfidTagList: List<TagMasterModel>, locationName: String) {
        viewModelScope.launch {
            val currentLocationId =
                locationMasterRepository.getLocationIdByName(locationName = locationName)
                    ?: 0
            val matchLocationScannedTags =
                rfidTagList.filter { it.newFields.tagStatus == TagStatus.PROCESSED && it.newFields.location == locationName }.map { it.epc }
                    .toSet()

            val notMatchLocationScannedTags =
                rfidTagList.filter { it.newFields.tagStatus == TagStatus.PROCESSED && it.newFields.location != locationName }.map { it.epc }
                    .toSet()

            val tagsInStock = tagMasterRepository.getTagsByLocationAndStock(
                locationId = currentLocationId,
                isInStock = true
            ).map { it.epc }.toList()


            val newList = rfidTagList.filter { it.newFields.tagStatus == TagStatus.PROCESSED }.map { tag ->
                val wrongLocation = notMatchLocationScannedTags.contains(tag.epc)
                val shortage = tag.epc in tagsInStock && tag.epc !in matchLocationScannedTags
                val over = tag.epc !in tagsInStock && tag.epc in matchLocationScannedTags
                val ok = tag.epc in tagsInStock && tag.epc in matchLocationScannedTags


                val resultType = when {
                    wrongLocation -> InventoryResultType.FOUND_WRONG_LOCATION
                    shortage -> InventoryResultType.NOT_FOUND
                    over -> InventoryResultType.FOUND_OVER_STOCK
                    ok -> InventoryResultType.FOUND_OK
                    else -> InventoryResultType.UNKNOWN
                }
                tag.copy(newFields = tag.newFields.copy(inventoryResultType = resultType))
            }
            _wrongLocationCount.value =
                newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_WRONG_LOCATION }
            _shortageCount.value =
                newList.count { it.newFields.inventoryResultType == InventoryResultType.NOT_FOUND }
            _overCount.value =
                newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_OVER_STOCK }
            _okCount.value =
                newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_OK }

            _finalTagList.value = newList
        }
    }

    suspend fun generateCsvData(
        memo: String,
        locationName: String
    ): List<InventoryResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            val locationId = locationMasterRepository.getLocationIdByName(locationName)
            _finalTagList.value.forEach { tag ->
                val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                val inventoryResultTypeId =
                    inventoryResultTypeRepository.getInventoryResultTypeIdByCode(
                        tag.newFields.inventoryResultType.name
                    )
                val model = InventoryResultCsvModel(
                    locationId = locationId ?: 0,
                    inventoryResultTypeId = inventoryResultTypeId,
                    ledgerItemId = ledgerId ?: 0,
                    tagId = tag.tagId,
                    deviceId = Build.ID,
                    memo = memo,
                    scannedAt = if (inventoryResultTypeId == 3) "" else generateIso8601JstTimestamp(),
                    executedAt = generateIso8601JstTimestamp()
                )
                csvModels.add(model)
            }
            csvModels.toList()
        }

    suspend fun saveInventoryResultToDb(memo: String, locationName: String): Result<Int> {
        return try {
            var sessionId = 0
            val locationId = locationMasterRepository.getLocationIdByName(locationName)
            inventoryCompleteRepository.saveInventoryResultLocalTransaction {
                sessionId = inventoryCompleteRepository.createInventorySession(
                    locationId = locationId
                )
                inventoryCompleteRepository.insertInventoryResultLocal(
                    sessionId = sessionId,
                    memo = memo,
                    tagList = _finalTagList.value
                )
            }

            Result.success(sessionId)
        } catch (e: Exception) {
            Log.e("TSS", "saveInventoryResultToDb: ${e.message}")
            Result.failure(e)
        }
    }
}