package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
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
        occurrenceReason: String,
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
                occurrenceReason = occurrenceReason,
                occurredAt = generateIso8601JstTimestamp(),
                registeredAt = generateIso8601JstTimestamp()
            )
            csvModels.add(model)
            csvModels.toList()
        }

    suspend fun saveInboundToDb(
        weight: String,
        grade: String,
        specificGravity: String,
        thickness: String,
        width: String,
        length: String,
        quantity: String,
        winderInfo: String,
        occurrenceReason: String,
        memo: String,
        rfidTag: TagMasterModel?
    ): Result<Int> {
        return try {
            val sessionId = inboundRepository.saveInboundToDb(
                memo = memo,
                weight = weight,
                grade = grade,
                specificGravity = specificGravity,
                thickness = thickness,
                width = width,
                length = length,
                quantity = quantity,
                winderInfo = winderInfo,
                occurrenceReason = occurrenceReason,
                rfidTag = rfidTag,
            )
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}