package com.example.sol_denka_stockmanagement.domain.usecase.outbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundSessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import javax.inject.Inject

class SaveOutboundUseCase @Inject constructor(
    private val sessionRepo: IOutboundSessionRepository,
    private val eventRepo: IOutboundEventRepository,
    private val tagRepo: ITagMasterRepository,
    private val processTypeRepo: IProcessTypeRepository,
    private val db: AppDatabase
) {
    suspend fun createSession(executedAt: String): Int =
        sessionRepo.insert(
            OutboundSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertEvents(
        sessionId: Int,
        memo: String?,
        processedAt: String?,
        registeredAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        tags: List<TagMasterModel>
    ) {
        tags.forEach { tag ->
            val ledgerId = tagRepo.getLedgerIdByTagId(tag.tagId)
            val processTypeId = processTypeRepo.getIdByName(tag.newFields.processType)
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

    suspend fun withTransaction(block: suspend () -> Unit) =
        db.withTransaction { block() }
}