package com.example.sol_denka_stockmanagement.database.repository.inbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: InboundSessionRepository,
    private val eventRepo: InboundEventRepository,
    private val tagMasterRepository: TagMasterRepository,
    private val ledgerItemRepository: LedgerItemRepository
) {

    suspend fun saveInboundToDb(
        weight: Int?,
        width: Int?,
        length: Int?,
        thickness: Int?,
        lotNo: String?,
        occurrenceReason: String?,
        quantity: Int?,
        memo: String?,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        rfidTag: TagMasterModel?
    ): Int = db.withTransaction {
        val sessionId = sessionRepo.insert(
            InboundSessionModel(
                deviceId = Build.ID,
                executedAt = generateIso8601JstTimestamp(),
            )
        )
        sessionId.let {
            val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(
                rfidTag?.tagId ?: 0
            )
            val winderId = ledgerItemRepository.getWinderIdByLedgerId(ledgerId = rfidTag?.ledgerItemId ?: 0)
            eventRepo.insert(
                InboundEventModel(
                    inboundSessionId = sessionId.toInt(),
                    itemTypeId = itemTypeId,
                    locationId = locationId,
                    winderId = winderId,
                    tagId = rfidTag?.tagId ?: 0,
                    weight = weight,
                    width =  width,
                    length = length,
                    thickness = thickness,
                    lotNo = lotNo,
                    occurrenceReason = occurrenceReason,
                    quantity = quantity,
                    memo = memo,
                    occurredAt = occurredAt,
                    processedAt = processedAt,
                    registeredAt = registeredAt
                )
            )
            sessionId.toInt()
        }
    }
}
