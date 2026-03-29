package com.example.sol_denka_stockmanagement.domain.usecase.inbound

import android.os.Build
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.domain.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class SaveInboundUseCase @Inject constructor(
    private val inboundRepository: IInboundRepository,
    private val itemTypeRepository: IItemTypeRepository
) {
    suspend fun saveToDb(
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
    ): Result<Int> = runCatching {
        var sessionId = 0
        inboundRepository.saveInboundTransaction {
            sessionId = inboundRepository.createInboundSession(executedAt = executedAt)
            val itemTypeId = itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)
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
        sessionId
    }

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
    ): List<InboundResultCsvModel> = withContext(Dispatchers.IO) {
        runCatching {
            val itemTypeId = itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)
            listOf(
                InboundResultCsvModel(
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
            )
        }.getOrDefault(emptyList())
    }

    private fun normalizeThickness(raw: String?): BigDecimal? {
        if (raw.isNullOrBlank()) return null
        val bd = raw.toBigDecimalOrNull()
            ?: throw IllegalArgumentException("thickness is not a number")
        return bd.setScale(3, RoundingMode.DOWN)
    }
}