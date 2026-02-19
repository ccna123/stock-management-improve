package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType

data class OutboundResultCsvModel(
    val ledgerItemId: Int,
    val tagId: Int,
    val processTypeId: Int,
    val deviceId: String,
    val memo: String?,
    val sourceEventId: String,
    val processedAt: String?,
    val registeredAt: String,
    val timeStamp: String
) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        "ledger_item_id",
        "tag_id",
        "process_type_id",
        "device_id",
        "source_event_id",
        "memo",
        "processed_at",
        "registered_at"
    )

    override fun toRow(): List<String> = listOf(
        ledgerItemId.toString(),
        tagId.toString(),
        processTypeId.toString(),
        deviceId,
        sourceEventId,
        memo ?: "",
        processedAt ?: "",
        registeredAt
    )

    override fun toCsvType(): String = CsvType.OutboundResult.displayNameJp

    override fun toCsvName(): String = "outbound_result_${timeStamp}.csv"
}