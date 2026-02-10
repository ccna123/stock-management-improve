package com.example.sol_denka_stockmanagement.screen.inventory.complete

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryCompleteRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.InventoryResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class InventoryCompleteViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
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

    suspend fun generateCsvData(
        memo: String,
        sourceSessionUuid: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ): List<InventoryResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                rfidTagList.forEach { tag ->
                    val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val model = InventoryResultCsvModel(
                        sourceSessionId = sourceSessionUuid,
                        locationId = locationId,
                        ledgerItemId = ledgerId,
                        tagId = tag.tagId,
                        deviceId = Build.ID,
                        memo = memo,
                        scannedAt = generateIso8601JstTimestamp(),
                        completedAt = generateIso8601JstTimestamp()
                    )
                    csvModels.add(model)
                }
                csvModels.toList()
            } catch (e: Exception) {
                Log.e("TSS", "generateCsvData: ${e.message}")
                emptyList()
            }
        }

    suspend fun saveInventoryResultToDb(
        memo: String,
        sourceSessionUuid: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> {
        return try {
            var sessionId = 0
            inventoryCompleteRepository.saveInventoryResultTransaction {
                sessionId = inventoryCompleteRepository.createInventorySession(
                    locationId = locationId,
                    sourceSessionUuid = sourceSessionUuid,
                    memo = memo
                )
                inventoryCompleteRepository.insertInventoryDetail(
                    sessionId = sessionId,
                    tagList = rfidTagList
                )
            }

            Result.success(sessionId)
        } catch (e: Exception) {
            Log.e("TSS", "saveInventoryResultToDb: ${e.message}")
            Result.failure(e)
        }
    }
}