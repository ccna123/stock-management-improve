package com.example.sol_denka_stockmanagement.domain.repository.inventory

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IInventoryCompleteRepository @Inject constructor(
    private val db: AppDatabase,
    private val inventorySessionRepository: IInventorySessionRepository,
    private val tagMasterRepository: ITagMasterRepository,
    private val inventoryDetailRepository: IInventoryDetailRepository
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
        tagList: List<TagMasterModel>
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
