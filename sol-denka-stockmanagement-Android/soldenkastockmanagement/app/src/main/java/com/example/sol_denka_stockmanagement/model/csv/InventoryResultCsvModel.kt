package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class InventoryResultCsvModel(
    val locationId: Int,
    val inventoryResultTypeId: Int,
    val ledgerItemId: Int,
    val tagId: Int,
    val deviceId: String,
    val memo: String,
    val scannedAt: String,
    val executedAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        "location_id",
        "inventory_result_type_id",
        "ledger_item_id",
        "tag_id",
        "device_id",
        "memo",
        "scanned_at",
        "executed_at"
    )

    override fun toRow(): List<String> = listOf(
        locationId.toString(),
        inventoryResultTypeId.toString(),
        ledgerItemId.toString(),
        tagId.toString(),
        deviceId,
        memo,
        scannedAt,
        executedAt
    )

    override fun toCsvType(): String = CsvType.InventoryResult.displayName

    override fun toCsvName(): String = "inventory_result_${generateTimeStamp()}.csv"
}
