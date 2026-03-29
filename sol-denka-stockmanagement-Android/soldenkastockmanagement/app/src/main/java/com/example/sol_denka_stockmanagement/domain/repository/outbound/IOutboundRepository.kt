package com.example.sol_denka_stockmanagement.domain.repository.outbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IOutboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: IOutboundSessionRepository,
    private val eventRepo: IOutboundEventRepository,
    private val tagMasterRepository: ITagMasterRepository,
    private val processTypeRepository: IProcessTypeRepository
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