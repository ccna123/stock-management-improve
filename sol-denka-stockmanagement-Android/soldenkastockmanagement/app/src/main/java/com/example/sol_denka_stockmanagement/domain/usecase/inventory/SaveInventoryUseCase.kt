package com.example.sol_denka_stockmanagement.domain.usecase.inventory

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryDetailRepository
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventorySessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import javax.inject.Inject

class SaveInventoryUseCase @Inject constructor(
    private val sessionRepo: IInventorySessionRepository,
    private val detailRepo: IInventoryDetailRepository,
    private val tagRepo: ITagMasterRepository,
    private val db: AppDatabase
) {
    suspend fun createSession(
        locationId: Int,
        memo: String?,
        executedAt: String,
        sourceSessionUuid: String
    ): Int =
        sessionRepo.insert(
            InventorySessionModel(
                sourceSessionUuid = sourceSessionUuid,
                deviceId = Build.ID,
                memo = memo,
                locationId = locationId,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertDetails(
        sessionId: Int,
        scannedAt: String,
        tagList: List<TagMasterModel>
    ) {
        tagList.forEach { tag ->
            val ledgerItemId = tagRepo.getLedgerIdByTagId(tag.tagId)
            detailRepo.insert(
                InventoryDetailModel(
                    inventorySessionId = sessionId,
                    ledgerItemId = ledgerItemId,
                    tagId = tag.tagId,
                    scannedAt = scannedAt
                )
            )
        }
    }

    suspend fun withTransaction(block: suspend () -> Unit) =
        db.withTransaction { block() }
}