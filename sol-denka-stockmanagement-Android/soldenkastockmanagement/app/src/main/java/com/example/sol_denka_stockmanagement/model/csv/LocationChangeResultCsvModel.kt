package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType

data class LocationChangeResultCsvModel(
    val ledgerItemId: Int,
    val locationId: Int,
    val deviceId: String,
    val memo: String?,
    val sourceEventId: String,
    val scannedAt: String,
    val executedAt: String,
    val timeStamp: String
) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        "ledger_item_id",
        "location_id",
        "device_id",
        "memo",
        "source_event_id",
        "scanned_at",
        "executed_at"
    )

    override fun toRow(): List<String> = listOf(
        ledgerItemId.toString(),
        locationId.toString(),
        deviceId,
        memo ?: "",
        sourceEventId,
        scannedAt,
        executedAt
    )

    override fun toCsvType(): String = CsvType.LocationChangeResult.displayNameJp

    override fun toCsvName(): String = "location_change_result_${timeStamp}.csv"
}
