package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundRepository
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
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
    private val ledgerItemRepository: LedgerItemRepository
) : ViewModel() {

    private val csvModels = mutableListOf<InboundResultCsvModel>()

    suspend fun generateCsvData(
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
            val winderId = ledgerItemRepository.getWinderIdByLedgerId(ledgerId = rfidTag?.ledgerItemId ?: 0)
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
            val sessionId = inboundRepository.saveInboundToDb(
                winder = winder?.takeIf { it.isNotBlank() },
                weight = weight?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                width = width?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                length = length?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                thickness = thickness?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                lotNo = lotNo?.takeIf { it.isNotBlank() },
                occurrenceReason = occurrenceReason?.takeIf { it.isNotBlank() },
                quantity = quantity?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                memo = memo?.takeIf { it.isNotBlank() },
                occurredAt = occurredAt?.takeIf { it.isNotBlank() },
                processedAt = processedAt?.takeIf { it.isNotBlank() },
                registeredAt = registeredAt,
                rfidTag = rfidTag,
            )
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}