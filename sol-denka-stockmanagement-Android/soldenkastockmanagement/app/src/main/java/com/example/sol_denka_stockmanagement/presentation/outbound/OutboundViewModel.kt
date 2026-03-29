package com.example.sol_denka_stockmanagement.presentation.outbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.domain.repository.outbound.OutboundRepository
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val ITagMasterRepository: ITagMasterRepository,
    private val IProcessTypeRepository: IProcessTypeRepository,
    private val outboundRepository: OutboundRepository
) : ViewModel() {

    private val csvModels = mutableListOf<OutboundResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        processedAt: String?,
        registeredAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): List<OutboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                rfidTagList.forEach { tag ->
                    val processTypeId = IProcessTypeRepository.getIdByName(tag.newFields.processType)
                    val ledgerId = ITagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val model = OutboundResultCsvModel(
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
                    csvModels.add(model)
                }
                csvModels.toList()
            }catch (e: Exception){
                Log.e("TSS", "generateCsvData: ${e.message}")
                emptyList()
            }
        }

    suspend fun saveOutboundToDb(
        memo: String?,
        processedAt: String?,
        registeredAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> {
        return try {
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
            Result.success(sessionId)
        } catch (e: Exception) {
            Log.e("TSS", "saveOutboundToDb: ${e.message}")
            Result.failure(e)
        }
    }
}