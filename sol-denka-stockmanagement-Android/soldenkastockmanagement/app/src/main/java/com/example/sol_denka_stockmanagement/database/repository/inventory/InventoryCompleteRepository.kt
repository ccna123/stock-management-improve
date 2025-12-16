package com.example.sol_denka_stockmanagement.database.repository.inventory

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultLocalModel
import com.example.sol_denka_stockmanagement.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryCompleteRepository @Inject constructor(
    private val db: AppDatabase,
    private val inventorySessionRepository: InventorySessionRepository,
    private val inventoryResultTypeRepository: InventoryResultTypeRepository,
    private val inventoryResultLocalRepository: InventoryResultLocalRepository,
    private val tagMasterRepository: TagMasterRepository,
) {

    suspend fun createInventorySession(locationId: Int?): Int =
        inventorySessionRepository.insert(
            InventorySessionModel(
                deviceId = Build.ID,
                isExported = false,
                locationId = locationId ?: 0,
                executedAt = generateIso8601JstTimestamp(),
            )
        ).toInt()

    suspend fun insertInventoryResultLocal(
        sessionId: Int,
        memo: String,
        tagList: List<TagMasterModel>
    ) {
        tagList.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByEpc(tag.epc)
            val inventoryResultTypeId =
                inventoryResultTypeRepository.getInventoryResultTypeIdByCode(
                    tag.newFields.inventoryResultType.name
                )
            inventoryResultLocalRepository.insert(
                InventoryResultLocalModel(
                    inventorySessionId = sessionId,
                    inventoryResultTypeId = inventoryResultTypeId,
                    ledgerItemId = ledgerId ?: 0,
                    tagId = tag.tagId,
                    memo = memo,
                    scannedAt = if (inventoryResultTypeId == 3) "" else generateIso8601JstTimestamp()
                )
            )
        }
    }

    suspend fun saveInventoryResultLocalTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}
