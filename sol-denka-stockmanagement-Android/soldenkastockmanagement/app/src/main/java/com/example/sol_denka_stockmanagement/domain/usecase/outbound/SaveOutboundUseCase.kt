package com.example.sol_denka_stockmanagement.domain.usecase.outbound

import com.example.sol_denka_stockmanagement.domain.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// domain/usecase/outbound/SaveOutboundUseCase.kt
class SaveOutboundUseCase @Inject constructor(
    private val outboundRepository: OutboundRepository,
    private val tagRepo: ITagMasterRepository,
    private val processTypeRepo: IProcessTypeRepository
) {
    suspend fun saveToDb(
        memo: String?,
        processedAt: String?,
        registeredAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> = runCatching {
        var sessionId = 0
        outboundRepository.saveOutboundTransaction {
            sessionId = outboundRepository.createOutboundSession(executedAt = executedAt)
            outboundRepository.insertOutboundEvent(
                sessionId = sessionId,
                memo = memo,
                sourceEventIdByTagId = sourceEventIdByTagId,
                processedAt = processedAt,
                registeredAt = registeredAt,
                tags = rfidTagList
            )
        }
        sessionId
    }

    suspend fun generateCsvData(
        memo: String,
        processedAt: String?,
        registeredAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): List<OutboundResultCsvModel> = withContext(Dispatchers.IO) {
        rfidTagList.mapNotNull { tag ->
            runCatching {
                val processTypeId = processTypeRepo.getIdByName(tag.newFields.processType)
                val ledgerId = tagRepo.getLedgerIdByTagId(tag.tagId)
                OutboundResultCsvModel(
                    ledgerItemId = ledgerId,
                    tagId = tag.tagId,
                    processTypeId = processTypeId,
                    deviceId = Build.ID,
                    memo = memo,
                    sourceEventId = sourceEventIdByTagId[tag.tagId] ?: "",
                    processedAt = processedAt,
                    registeredAt = registeredAt,
                    timeStamp = formatTimestamp(registeredAt)
                )
            }.getOrNull()
        }
    }
}