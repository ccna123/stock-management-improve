package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.OutboundResultCsvHeader
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class OutboundResultCsvModel(
    val ledgerItemId: Int,
    val tagId: Int,
    val processTypeId: Int,
    val deviceId: String,
    val memo: String?,
    val processedAt: String?,
    val registeredAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        OutboundResultCsvHeader.ledger_item_id.name,
        OutboundResultCsvHeader.tag_id.name,
        OutboundResultCsvHeader.process_type_id.name,
        OutboundResultCsvHeader.device_id.name,
        OutboundResultCsvHeader.memo.name,
        OutboundResultCsvHeader.processed_at.name,
        OutboundResultCsvHeader.registerd_at.name
    )

    override fun toRow(): List<String> = listOf(
        ledgerItemId.toString(),
        tagId.toString(),
        processTypeId.toString(),
        deviceId,
        memo ?: "",
        processedAt ?:"",
        registeredAt
    )

    override fun toCsvType(): String = CsvType.OutboundResult.displayName

    override fun toCsvName(): String = "outbound_result_${generateTimeStamp()}.csv"
}
