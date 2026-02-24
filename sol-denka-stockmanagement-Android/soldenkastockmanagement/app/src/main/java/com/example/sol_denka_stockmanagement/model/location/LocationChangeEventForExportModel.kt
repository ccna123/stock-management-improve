package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel

data class LocationChangeEventForExportModel(
    val sourceEventId: String,
    val locationId: Int,
    val deviceId: String,
    val ledgerItemId: Int,
    val memo: String,
    val scannedAt: String,
    val executedAt: String,
)

fun LocationChangeEventForExportModel.toCsvModel(
    timeStamp: String,
) = LocationChangeResultCsvModel(
    ledgerItemId = ledgerItemId,
    locationId = locationId,
    deviceId = deviceId,
    memo = memo,
    sourceEventId = sourceEventId,
    executedAt = executedAt,
    scannedAt = scannedAt,
    timeStamp = timeStamp
)
