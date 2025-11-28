package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.InventoryResultCsvHeader
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class InventoryResultCsvModel(
    val locationId: String,
    val inventoryResultTypeId: String,
    val ledgerItemId: String,
    val tagId: String,
    val deviceId: String,
    val memo: String,
    val scannedAt: String,
    val executedAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        InventoryResultCsvHeader.location_id.name,
        InventoryResultCsvHeader.inventory_result_type_id.name,
        InventoryResultCsvHeader.ledger_item_id.name,
        InventoryResultCsvHeader.tag_id.name,
        InventoryResultCsvHeader.device_id.name,
        InventoryResultCsvHeader.memo.name,
        InventoryResultCsvHeader.scanned_at.name,
        InventoryResultCsvHeader.executed_at.name
    )

    override fun toRow(): List<String> = listOf(
        locationId,
        inventoryResultTypeId,
        ledgerItemId,
        tagId,
        deviceId,
        memo,
        scannedAt,
        executedAt
    )

    override fun toCsvType(): String = CsvType.InventoryResult.displayName

    override fun toCsvName(): String = "inventory_result_${generateTimeStamp()}.csv"
}
