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
    suspend fun createOutboundSession(executedAt: String): Int =
        sessionRepo.insert(
            OutboundSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertOutboundEvent(
        sessionId: Int,
        memo: String?,
        processedAt: String?,
        registeredAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        tags: List<TagMasterModel>
    ) {
        tags.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
            val processTypeId = processTypeRepository.getIdByName(tag.newFields.processType)

            eventRepo.insert(
                OutBoundEventModel(
                    outboundSessionId = sessionId,
                    ledgerItemId = ledgerId,
                    processTypeId = processTypeId,
                    tagId = tag.tagId,
                    memo = memo,
                    sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                    processedAt = processedAt,
                    registeredAt = registeredAt
                )
            )
        }
    }

    suspend fun saveOutboundTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}