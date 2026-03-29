package com.example.sol_denka_stockmanagement.domain.usecase.inventory

import android.os.Build
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.domain.model.csv.InventoryResultCsvModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryCompleteRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// domain/usecase/inventory/SaveInventoryUseCase.kt
class SaveInventoryUseCase @Inject constructor(
    private val inventoryRepo: IInventoryCompleteRepository,
    private val tagRepo: ITagMasterRepository
) {
    suspend fun saveToDb(
        memo: String,
        sourceSessionUuid: String,
        scannedAt: String,
        executedAt: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> = runCatching {
        var sessionId = 0
        inventoryRepo.saveInventoryResultTransaction {
            sessionId = inventoryRepo.createInventorySession(
                locationId = locationId,
                sourceSessionUuid = sourceSessionUuid,
                memo = memo,
                executedAt = executedAt
            )
            inventoryRepo.insertInventoryDetail(
                sessionId = sessionId,
                tagList = rfidTagList,
                scannedAt = scannedAt
            )
        }
        sessionId
    }

    suspend fun generateCsvData(
        memo: String,
        sourceSessionUuid: String,
        scannedAt: String,
        executedAt: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ): List<InventoryResultCsvModel> = withContext(Dispatchers.IO) {
        rfidTagList.mapNotNull { tag ->
            runCatching {
                val ledgerId = tagRepo.getLedgerIdByTagId(tag.tagId)
                InventoryResultCsvModel(
                    sourceSessionId = sourceSessionUuid,
                    locationId = locationId,
                    ledgerItemId = ledgerId,
                    tagId = tag.tagId,
                    deviceId = Build.ID,
                    memo = memo,
                    scannedAt = scannedAt,
                    executedAt = executedAt,
                    timeStamp = formatTimestamp(executedAt)
                )
            }.getOrNull()
        }
    }
}