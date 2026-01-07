package com.example.sol_denka_stockmanagement.screen.inventory.complete

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryCompleteRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
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

            val newList =
                rfidTagList.map { tag ->
                    val wrongLocation =
                        tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && tag.newFields.location != locationName
                    val shortage =
                        tag.newFields.tagScanStatus != TagScanStatus.PROCESSED && tag.newFields.isInStock && tag.newFields.location == locationName
                    val over =
                        tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && tag.newFields.isInStock.not() && tag.newFields.location == locationName
                    val ok =
                        tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && tag.newFields.isInStock && tag.newFields.location == locationName


                    val resultType = when {
                        wrongLocation -> InventoryResultType.FOUND_WRONG_LOCATION
                        shortage -> InventoryResultType.NOT_FOUND
                        over -> InventoryResultType.FOUND_OVER_STOCK
                        ok -> InventoryResultType.FOUND_OK
                        else -> InventoryResultType.UNKNOWN
                    }
                    tag.copy(newFields = tag.newFields.copy(inventoryResultType = resultType))
                }
            Log.e("TSS", "newList: $newList" )
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
        locationId: Int
    ): List<InventoryResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                _finalTagList.value.forEach { tag ->
                    val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val inventoryResultTypeId =
                        inventoryResultTypeRepository.getInventoryResultTypeIdByCode(
                            tag.newFields.inventoryResultType.name
                        )
                    val model = InventoryResultCsvModel(
                        locationId = locationId,
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
            }catch (e: Exception){
                Log.e("TSS", "generateCsvData: ${e.message}")
                emptyList()
            }
        }

    suspend fun saveInventoryResultToDb(memo: String, locationId: Int): Result<Int> {
        return try {
            var sessionId = 0
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