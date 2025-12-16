package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderInfoRepository
import com.example.sol_denka_stockmanagement.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val inboundRepository: InboundRepository,
    private val winderInfoRepository: WinderInfoRepository
) : ViewModel() {

    private val csvModels = mutableListOf<InboundResultCsvModel>()

    suspend fun generateCsvData(
        winder: String?,
        weight: String,
        width: String,
        length: String,
        thickness: String,
        lotNo: String,
        occurrenceReason: String,
        quantity: String,
        memo: String,
        occurredAt: String,
        processedAt: String,
        rfidTag: TagMasterModel?
    ): List<InboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()
            val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(
                rfidTag?.tagId ?: 0
            )
            val winderId = winderInfoRepository.getIdByName(winderName = winder ?: "")
            val model = InboundResultCsvModel(
                tagId = rfidTag?.tagId ?: 0,
                itemTypeId = itemTypeId,
                locationId = locationId,
                winderId = winderId,
                deviceId = Build.ID,
                weight = weight,
                width = width,
                length = length,
                thickness = thickness,
                lotNo = lotNo,
                occurrenceReason = occurrenceReason,
                quantity = quantity,
                memo = memo,
                occurredAt = occurredAt,
                processedAt = processedAt,
                registeredAt = generateIso8601JstTimestamp()
            )
            csvModels.add(model)
            csvModels.toList()
        }

    suspend fun saveInboundToDb(
        winder: String?,
        weight: String?,
        thickness: String?,
        width: String?,
        length: String?,
        quantity: String?,
        lotNo: String?,
        occurrenceReason: String?,
        memo: String?,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        rfidTag: TagMasterModel?
    ): Result<Int> {
        return try {
            var sessionId = 0

            inboundRepository.saveInboundTransaction {

                // 1️⃣ create session
                sessionId = inboundRepository.createInboundSession()
                val winderId = winderInfoRepository.getIdByName(winderName = winder ?: "")

                // 2️⃣ insert event (only if session OK)
                inboundRepository.insertInboundEvent(
                    sessionId = sessionId,
                    winderId = winderId,
                    weight = weight?.takeIf { it.isNotBlank() }?.toInt(),
                    width = width?.takeIf { it.isNotBlank() }?.toInt(),
                    length = length?.takeIf { it.isNotBlank() }?.toInt(),
                    thickness = thickness?.takeIf { it.isNotBlank() }?.toInt(),
                    lotNo = lotNo?.takeIf { it.isNotBlank() },
                    occurrenceReason = occurrenceReason?.takeIf { it.isNotBlank() },
                    quantity = quantity?.takeIf { it.isNotBlank() }?.toInt(),
                    memo = memo?.takeIf { it.isNotBlank() },
                    occurredAt = occurredAt?.takeIf { it.isNotBlank() },
                    processedAt = processedAt?.takeIf { it.isNotBlank() },
                    registeredAt = registeredAt,
                    rfidTag = rfidTag
                )
            }
            Result.success(sessionId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}