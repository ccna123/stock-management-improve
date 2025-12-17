package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.InboundResultCsvHeader
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class InboundResultCsvModel(
    val tagId: Int,
    val itemTypeId: Int,
    val locationId: Int?,
    val winderId: Int?,
    val deviceId: String,
    val weight: String,
    val width: String,
    val length: String,
    val thickness: String,
    val lotNo: String,
    val occurrenceReason: String,
    val quantity: String,
    val memo: String,
    val occurredAt: String,
    val processedAt: String,
    val registeredAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        InboundResultCsvHeader.tag_id.name,
        InboundResultCsvHeader.item_type_id.name,
        InboundResultCsvHeader.location_id.name,
        InboundResultCsvHeader.winder_id.name,
        InboundResultCsvHeader.device_id.name,
        InboundResultCsvHeader.weight.name,
        InboundResultCsvHeader.width.name,
        InboundResultCsvHeader.length.name,
        InboundResultCsvHeader.thickness.name,
        InboundResultCsvHeader.lot_no.name,
        InboundResultCsvHeader.occurrence_reason.name,
        InboundResultCsvHeader.quantity.name,
        InboundResultCsvHeader.memo.name,
        InboundResultCsvHeader.occurred_at.name,
        InboundResultCsvHeader.processed_at.name,
        InboundResultCsvHeader.registerd_at.name
    )

    override fun toRow(): List<String> = listOf(
        tagId.toString(),
        itemTypeId.toString(),
        locationId?.toString() ?: "",
        winderId?.toString() ?: "",
        deviceId,
        weight,
        width,
        length,
        thickness,
        lotNo,
        occurrenceReason,
        quantity,
        memo,
        occurredAt,
        processedAt,
        registeredAt
    )

    override fun toCsvType(): String = CsvType.InboundResult.displayName

    override fun toCsvName(): String = "inbound_result_${generateTimeStamp()}.csv"
}
