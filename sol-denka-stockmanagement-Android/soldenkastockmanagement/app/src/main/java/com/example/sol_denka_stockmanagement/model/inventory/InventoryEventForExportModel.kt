package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.model.csv.InventoryResultCsvModel

data class InventoryEventForExportModel(
    val sourceSessionId: String,
    val locationId: Int,
    val deviceId: String,
    val ledgerItemId: Int,
    val tagId: Int,
    val memo: String,
    val scannedAt: String,
    val executedAt: String,
)

fun InventoryEventForExportModel.toCsvModel(
    timeStamp: String,
) = InventoryResultCsvModel(
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    scannedAt = scannedAt,
    sourceSessionId = sourceSessionId,
    locationId = locationId,
    deviceId = deviceId,
    memo = memo,
    executedAt = executedAt,
    timeStamp = timeStamp
)
