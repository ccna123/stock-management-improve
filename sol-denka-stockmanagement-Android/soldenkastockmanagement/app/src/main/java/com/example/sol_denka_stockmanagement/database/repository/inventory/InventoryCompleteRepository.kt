package com.example.sol_denka_stockmanagement.database.repository.inventory

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryCompleteRepository @Inject constructor(
    private val db: AppDatabase,
    private val inventorySessionRepository: InventorySessionRepository,
    private val tagMasterRepository: TagMasterRepository,
    private val inventoryDetailRepository: InventoryDetailRepository
) {

    suspend fun createInventorySession(
        locationId: Int,
        memo: String?,
        executedAt: String,
        sourceSessionUuid: String
    ): Int =
        inventorySessionRepository.insert(
            _root_ide_package_.com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel(
                sourceSessionUuid = sourceSessionUuid,
                deviceId = Build.ID,
                memo = memo,
                locationId = locationId,
                executedAt = executedAt,
            )
        ).toInt()

    suspend fun insertInventoryDetail(
        sessionId: Int,
        scannedAt: String,
        tagList: List<com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel>
    ) {
        tagList.forEach { tag ->
            val ledgerItemId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
            inventoryDetailRepository.insert(
                InventoryDetailModel(
                    inventorySessionId = sessionId,
                    ledgerItemId = ledgerItemId,
                    tagId = tag.tagId,
                    scannedAt = scannedAt,
                )
            )
        }
    }

    suspend fun saveInventoryResultTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}
