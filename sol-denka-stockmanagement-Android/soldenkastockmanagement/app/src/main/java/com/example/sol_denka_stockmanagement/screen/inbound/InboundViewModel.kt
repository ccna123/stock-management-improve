package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val inboundRepository: InboundRepository,
    private val itemTypeRepository: ItemTypeRepository,
) : ViewModel() {

    private val csvModels = mutableListOf<InboundResultCsvModel>()

    suspend fun generateCsvData(
        itemInCategory: String,
        locationId: Int,
        winderId: Int?,
        weight: String,
        width: String,
        length: String,
        thickness: String,
        lotNo: String,
        occurrenceReason: String,
        quantity: String,
        memo: String,
        sourceEventId: String,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        rfidTag: TagMasterModel?
    ): List<InboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                val itemTypeId =
                    itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)
                val model = InboundResultCsvModel(
                    tagId = rfidTag?.tagId ?: 0,
                    itemTypeId = itemTypeId,
                    locationId = locationId,
                    winderId = winderId,
                    deviceId = Build.ID,
                    weight = weight,
                    width = width,
                    length = length,
                    thickness = normalizeThickness(thickness)?.toString(),
                    lotNo = lotNo,
                    occurrenceReason = occurrenceReason,
                    quantity = quantity,
                    memo = memo,
                    sourceEventId = sourceEventId,
                    occurredAt = occurredAt?.takeIf { it.isNotBlank() },
                    processedAt = processedAt?.takeIf { it.isNotBlank() },
                    registeredAt = registeredAt,
                    timeStamp = formatTimestamp(registeredAt)
                )
                csvModels.add(model)
                csvModels.toList()
            } catch (e: Exception) {
                Log.e("TSS", "generateCsvData: ${e.message}")
                emptyList()
            }
        }

    suspend fun saveInboundToDb(
        itemInCategory: String,
        locationId: Int,
        winderId: Int?,
        weight: String?,
        thickness: String?,
        width: String?,
        length: String?,
        quantity: String?,
        lotNo: String?,
        occurrenceReason: String?,
        memo: String?,
        sourceEventId: String,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        executedAt: String,
        rfidTag: TagMasterModel?
    ): Result<Int> {
        return try {
            var sessionId = 0

            inboundRepository.saveInboundTransaction {

                // 1️⃣ create session
                sessionId = inboundRepository.createInboundSession(executedAt = executedAt)
                val itemTypeId =
                    itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)

                // 2️⃣ insert event (only if session OK)
                inboundRepository.insertInboundEvent(
                    sessionId = sessionId,
                    winderId = winderId,
                    itemTypeId = itemTypeId,
                    locationId = locationId,
                    weight = weight?.takeIf { it.isNotBlank() }?.toInt(),
                    width = width?.takeIf { it.isNotBlank() }?.toInt(),
                    length = length?.takeIf { it.isNotBlank() }?.toInt(),
                    thickness = normalizeThickness(thickness),
                    lotNo = lotNo?.takeIf { it.isNotBlank() },
                    occurrenceReason = occurrenceReason?.takeIf { it.isNotBlank() },
                    quantity = quantity?.takeIf { it.isNotBlank() }?.toInt(),
                    memo = memo?.takeIf { it.isNotBlank() },
                    sourceEventId = sourceEventId,
                    occurredAt = occurredAt?.takeIf { it.isNotBlank() },
                    processedAt = processedAt?.takeIf { it.isNotBlank() },
                    registeredAt = registeredAt,
                    rfidTag = rfidTag
                )
            }
            Result.success(sessionId)

        } catch (e: Exception) {
            Log.e("TSS", "saveInboundToDb: ${e.message}")
            Result.failure(e)
        }
    }
    private fun normalizeThickness(raw: String?): BigDecimal? {
        if (raw.isNullOrBlank()) return null

        val bd = raw.toBigDecimalOrNull()
            ?: throw IllegalArgumentException("thickness is not a number")

        return bd.setScale(3, RoundingMode.DOWN)
    }
}