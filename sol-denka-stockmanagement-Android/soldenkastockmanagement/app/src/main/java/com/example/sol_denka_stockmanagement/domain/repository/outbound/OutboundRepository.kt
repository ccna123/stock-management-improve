package com.example.sol_denka_stockmanagement.domain.repository.outbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: com.example.sol_denka_stockmanagement.domain.repository.outbound.OutboundSessionRepository,
    private val eventRepo: com.example.sol_denka_stockmanagement.domain.repository.outbound.OutboundEventRepository,
    private val ITagMasterRepository: ITagMasterRepository,
    private val IProcessTypeRepository: com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
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
            val ledgerId = ITagMasterRepository.getLedgerIdByTagId(tag.tagId)
            val processTypeId = IProcessTypeRepository.getIdByName(tag.newFields.processType)

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