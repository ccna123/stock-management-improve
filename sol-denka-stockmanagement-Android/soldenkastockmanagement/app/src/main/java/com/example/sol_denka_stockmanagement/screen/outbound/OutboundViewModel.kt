package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val outboundRepository: OutboundRepository
) : ViewModel() {

    private val csvModels = mutableListOf<OutboundResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        processedAt: String,
        registeredAt: String,
        rfidTagList: List<TagMasterModel>
    ): List<OutboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            rfidTagList.forEach { tag ->
                val processTypeId = processTypeRepository.getIdByName(tag.newFields.processType)
                val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                val model = OutboundResultCsvModel(
                    ledgerItemId = ledgerId ?: 0,
                    tagId = tag.tagId,
                    processTypeId = processTypeId,
                    deviceId = Build.ID,
                    memo = memo,
                    processedAt = processedAt,
                    registeredAt = registeredAt
                )
                csvModels.add(model)
            }
            csvModels.toList()
        }

    suspend fun saveOutboundToDb(
        memo: String?,
        processedAt: String,
        registeredAt: String,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> {
        return try {
            var sessionId = 0
            outboundRepository.saveOutboundTransaction {

                sessionId = outboundRepository.createOutboundSession()

                outboundRepository.insertOutboundEvent(
                    sessionId = sessionId,
                    memo = memo,
                    processedAt = processedAt,
                    registeredAt = registeredAt,
                    tags = rfidTagList
                )
            }
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}