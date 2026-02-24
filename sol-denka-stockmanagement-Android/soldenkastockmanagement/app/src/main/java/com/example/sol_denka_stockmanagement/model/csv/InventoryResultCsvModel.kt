package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType

data class InventoryResultCsvModel(
    val sourceSessionId: String,
    val locationId: Int,
    val deviceId: String,
    val ledgerItemId: Int,
    val tagId: Int,
    val memo: String?,
    val scannedAt: String,
    val executedAt: String,
    val timeStamp: String

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        "source_session_id",
        "location_id",
        "device_id",
        "ledger_item_id",
        "tag_id",
        "memo",
        "scanned_at",
        "executed_at"
    )

    override fun toRow(): List<String> = listOf(
        sourceSessionId,
        locationId.toString(),
        deviceId,
        ledgerItemId.toString(),
        tagId.toString(),
        memo ?: "",
        scannedAt,
        executedAt
    )

    override fun toCsvType(): String = CsvType.InventoryResult.displayNameJp

    override fun toCsvName(): String = "inventory_result_${timeStamp}.csv"
}
