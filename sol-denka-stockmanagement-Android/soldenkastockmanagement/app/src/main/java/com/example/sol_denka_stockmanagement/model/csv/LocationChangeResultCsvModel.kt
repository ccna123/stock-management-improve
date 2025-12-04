package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.LocationChangeResultCsvHeader
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class LocationChangeResultCsvModel(
    val ledgerItemId: Int,
    val locationId: Int,
    val deviceId: String,
    val memo: String,
    val scannedAt: String,
    val executedAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        LocationChangeResultCsvHeader.ledger_item_id.name,
        LocationChangeResultCsvHeader.location_id.name,
        LocationChangeResultCsvHeader.device_id.name,
        LocationChangeResultCsvHeader.memo.name,
        LocationChangeResultCsvHeader.scanned_at.name,
        LocationChangeResultCsvHeader.executed_at.name
    )

    override fun toRow(): List<String> = listOf(
        ledgerItemId.toString(),
        locationId.toString(),
        deviceId,
        memo,
        scannedAt,
        executedAt
    )

    override fun toCsvType(): String = CsvType.LocationChangeResult.displayName

    override fun toCsvName(): String = "location_change_result_${generateTimeStamp()}.csv"
}
