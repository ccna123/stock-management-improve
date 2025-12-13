package com.example.sol_denka_stockmanagement.database.repository.outbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: OutboundSessionRepository,
    private val eventRepo: OutboundEventRepository,
    private val tagMasterRepository: TagMasterRepository,
    private val processTypeRepository: ProcessTypeRepository
) {

    suspend fun saveOutbound(
        memo: String?,
        processedAt: String,
        registeredAt: String,
        tags: List<TagMasterModel>
    ): Int = db.withTransaction {

        // 1. Insert session
        val sessionId = sessionRepo.insert(
            OutboundSessionModel(
                deviceId = Build.ID,
                executedAt = generateIso8601JstTimestamp()
            )
        )
        // 2. Insert all events
        tags.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByEpc(tag.epc)
            val processTypeId = processTypeRepository.getIdByName(tag.newFields.processType)

            eventRepo.insert(
                OutBoundEventModel(
                    outboundSessionId = sessionId.toInt(),
                    ledgerItemId = ledgerId ?: 0,
                    processTypeId = processTypeId,
                    memo = memo,
                    processedAt = processedAt,
                    registeredAt = registeredAt
                )
            )
        }
        sessionId.toInt()
    }
}
