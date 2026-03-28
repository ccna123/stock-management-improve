package com.example.sol_denka_stockmanagement.domain.model.inbound

import com.example.sol_denka_stockmanagement.domain.model.csv.InboundResultCsvModel
import java.math.BigDecimal

data class InboundEventModel(
    val inboundEventId: Int = 0,
    val inboundSessionId: Int,
    val itemTypeId: Int,
    val locationId: Int,
    val winderId: Int?,
    val tagId: Int,
    val sourceEventId: String,
    val weight: Int?,
    val width: Int?,
    val length: Int?,
    val thickness: BigDecimal?,
    val lotNo: String?,
    val occurrenceReason: String?,
    val quantity: Int?,
    val memo: String?,
    val occurredAt: String?,
    val processedAt: String?,
    val registeredAt: String,
)

fun InboundEventModel.toCsvModel(
    deviceId: String,
    timeStamp: String
): InboundResultCsvModel {

    return InboundResultCsvModel(
        tagId = tagId,
        itemTypeId = itemTypeId,
        locationId = locationId,
        winderId = winderId,
        deviceId = deviceId,
        weight = weight?.toString() ?: "",
        width = width?.toString() ?: "",
        length = length?.toString() ?: "",
        thickness = thickness?.toPlainString(),
        lotNo = lotNo ?: "",
        occurrenceReason = occurrenceReason ?: "",
        quantity = quantity?.toString() ?: "",
        memo = memo ?: "",
        sourceEventId = sourceEventId,
        occurredAt = occurredAt,
        processedAt = processedAt,
        registeredAt = registeredAt,
        timeStamp = timeStamp
    )
}

