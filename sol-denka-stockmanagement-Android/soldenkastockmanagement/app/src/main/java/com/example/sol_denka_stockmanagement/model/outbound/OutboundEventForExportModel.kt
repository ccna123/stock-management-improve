package com.example.sol_denka_stockmanagement.model.outbound

import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel

data class OutboundEventForExportModel(
    val ledgerItemId: Int,
    val tagId: Int,
    val processTypeId: Int,
    val deviceId: String,
    val sourceEventId: String,
    val memo: String,
    val processedAt: String,
    val registeredAt: String,
)

fun OutboundEventForExportModel.toCsvModel(
    timeStamp: String,
) = OutboundResultCsvModel(
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    processTypeId = processTypeId,
    deviceId = deviceId,
    sourceEventId = sourceEventId,
    memo = memo,
    processedAt = processedAt,
    registeredAt = registeredAt,
    timeStamp = timeStamp
)
