package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundEventRepository
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val outboundSessionRepository: OutboundSessionRepository,
    private val outboundEventRepository: OutboundEventRepository
) : ViewModel() {

    private val csvModels = mutableListOf<InboundResultCsvModel>()

    suspend fun generateCsvData(
        weight: String,
        grade: String,
        specificGravity: String,
        thickness: String,
        width: String,
        length: String,
        quantity: String,
        winderInfo: String,
        misrollReason: String,
        rfidTag: TagMasterModel?
    ): List<InboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(rfidTag?.tagId ?: 0)
            val model = InboundResultCsvModel(
                tagId = rfidTag?.tagId ?: 0,
                itemTypeId = itemTypeId,
                locationId = locationId,
                deviceId = Build.ID,
                weight = weight,
                grade = grade,
                specificGravity = specificGravity,
                thickness = thickness,
                width = width,
                length = length,
                quantity = quantity,
                winderInfo = winderInfo,
                misrollReason = misrollReason,
                occurredAt = generateIso8601JstTimestamp(),
                registeredAt = generateIso8601JstTimestamp()
            )
            csvModels.add(model)
            csvModels.toList()
        }

    suspend fun saveOutboundToDb(
        memo: String,
        occurredAt: String,
        rfidTagList: List<TagMasterModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val sessionId = outboundSessionRepository.insert(
                    OutboundSessionModel(
                        deviceId = Build.ID,
                        executedAt = generateIso8601JstTimestamp(),
                    )
                )
                sessionId.let {
                    rfidTagList.forEach { tag ->
                        val ledgerId = tagMasterRepository.getLedgerIdByEpc(tag.epc)
                        val processTypeId =
                            processTypeRepository.getIdByName(tag.newFields.processType)
                        val model = OutBoundEventModel(
                            outboundSessionId = sessionId.toInt(),
                            ledgerItemId = ledgerId ?: 0,
                            processTypeId = processTypeId,
                            memo = memo,
                            occurredAt = occurredAt,
                            registeredAt = generateIso8601JstTimestamp()
                        )
                        outboundEventRepository.insert(model)
                    }
                }
            } catch (e: Exception) {
                Log.e("TSS", "saveOutboundToDb: ${e.message}")
            }
        }
    }
}