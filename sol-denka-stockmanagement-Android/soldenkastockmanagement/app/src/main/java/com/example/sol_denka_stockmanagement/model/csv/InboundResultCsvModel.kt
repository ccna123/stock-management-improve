package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.InboundResultCsvHeader
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp

data class InboundResultCsvModel(
    val tagId: String,
    val itemTypeId: String,
    val locationId: String,
    val deviceId: String,
    val weight: String,
    val grade: String,
    val specificGravity: String,
    val thickness: String,
    val width: String,
    val length: String,
    val quantity: String,
    val winderInfo: String,
    val misrollReason: String,
    val occurredAt: String,
    val registeredAt: String,

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        InboundResultCsvHeader.tag_id.name,
        InboundResultCsvHeader.item_type_id.name,
        InboundResultCsvHeader.location_id.name,
        InboundResultCsvHeader.device_id.name,
        InboundResultCsvHeader.weight.name,
        InboundResultCsvHeader.grade.name,
        InboundResultCsvHeader.specific_gravity.name,
        InboundResultCsvHeader.thickness.name,
        InboundResultCsvHeader.width.name,
        InboundResultCsvHeader.length.name,
        InboundResultCsvHeader.quantity.name,
        InboundResultCsvHeader.winder_info.name,
        InboundResultCsvHeader.missroll_reason.name,
        InboundResultCsvHeader.occurred_at.name,
        InboundResultCsvHeader.registerd_at.name
    )

    override fun toRow(): List<String> = listOf(
        tagId,
        itemTypeId,
        locationId,
        deviceId,
        weight,
        grade,
        specificGravity,
        thickness,
        width,
        length,
        quantity,
        winderInfo,
        misrollReason,
        occurredAt,
        registeredAt
    )

    override fun toCsvType(): String = CsvType.InboundResult.displayName

    override fun toCsvName(): String = "inbound_result_${generateTimeStamp()}.csv"
}
