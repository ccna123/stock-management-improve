package com.example.sol_denka_stockmanagement.database.repository.inventory

import android.os.Build
import android.util.Log
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultLocalModel
import com.example.sol_denka_stockmanagement.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach

@Singleton
class InventoryCompleteRepository @Inject constructor(
    private val db: AppDatabase,
    private val locationMasterRepository: LocationMasterRepository,
    private val inventorySessionRepository: InventorySessionRepository,
    private val inventoryResultTypeRepository: InventoryResultTypeRepository,
    private val inventoryResultLocalRepository: InventoryResultLocalRepository,
    private val tagMasterRepository: TagMasterRepository,
) {

    suspend fun saveInventoryResultToDb(
        memo: String,
        locationName: String,
        tagList: List<TagMasterModel>
    ): Int = db.withTransaction {
        val locationId = locationMasterRepository.getLocationIdByName(locationName)
        val sessionId = inventorySessionRepository.insert(
            InventorySessionModel(
                deviceId = Build.ID,
                locationId = locationId ?: 0,
                executedAt = generateIso8601JstTimestamp(),
            )
        )

        tagList.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByEpc(tag.epc)
            val inventoryResultTypeId =
                inventoryResultTypeRepository.getInventoryResultTypeIdByCode(
                    tag.newFields.inventoryResultType.name
                )
            inventoryResultLocalRepository.insert(
                InventoryResultLocalModel(
                    inventorySessionId = sessionId.toInt(),
                    inventoryResultTypeId = inventoryResultTypeId,
                    ledgerItemId = ledgerId ?: 0,
                    tagId = tag.tagId,
                    memo = memo,
                    scannedAt = if (inventoryResultTypeId == 3) "" else generateIso8601JstTimestamp()
                )
            )
        }
        sessionId.toInt()
    }
}
