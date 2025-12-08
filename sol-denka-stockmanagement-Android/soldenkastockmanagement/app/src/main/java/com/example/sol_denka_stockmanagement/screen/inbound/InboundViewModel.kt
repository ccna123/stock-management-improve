package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundEventRepository
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val inboundSessionRepository: InboundSessionRepository,
    private val inboundEventRepository: InboundEventRepository
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
        missRollReason: String,
        rfidTag: TagMasterModel?
    ): List<InboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(
                rfidTag?.tagId ?: 0
            )
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
                missRollReason = missRollReason,
                occurredAt = generateIso8601JstTimestamp(),
                registeredAt = generateIso8601JstTimestamp()
            )
            csvModels.add(model)
            csvModels.toList()
        }

    suspend fun saveInboundToDb(
        weight: String,
        grade: String,
        thickness: String,
        length: String,
        winderInfo: String,
        memo: String,
        rfidTag: TagMasterModel?
    ) {
        withContext(Dispatchers.IO) {
            try {
                val sessionId = inboundSessionRepository.insert(
                    InboundSessionModel(
                        deviceId = Build.ID,
                        executedAt = generateIso8601JstTimestamp(),
                    )
                )
                sessionId.let {
                    val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(
                        rfidTag?.tagId ?: 0
                    )
                    val model = InboundEventModel(
                        inboundSessionId = sessionId.toInt(),
                        itemTypeId = itemTypeId,
                        locationId = locationId,
                        tagId = rfidTag?.tagId ?: 0,
                        weight = weight.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                        grade = grade,
                        specificGravity = "",
                        thickness = thickness.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                        width = 0,
                        length = length.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                        quantity = 0,
                        winderInfo = winderInfo,
                        missRollReason = "",
                        memo = memo,
                        occurredAt = generateIso8601JstTimestamp(),
                        registeredAt = generateIso8601JstTimestamp()
                    )
                    inboundEventRepository.insert(model)
                }
            } catch (e: Exception) {
                Log.e("TSS", "saveInboundToDb: ${e.message}")
            }
        }
    }
}