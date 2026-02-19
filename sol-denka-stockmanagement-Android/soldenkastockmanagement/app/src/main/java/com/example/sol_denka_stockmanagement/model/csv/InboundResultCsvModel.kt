package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType

data class InboundResultCsvModel(
    val tagId: Int,
    val itemTypeId: Int,
    val locationId: Int?,
    val winderId: Int?,
    val deviceId: String,
    val weight: String?,
    val width: String?,
    val length: String?,
    val thickness: String?,
    val lotNo: String?,
    val occurrenceReason: String?,
    val quantity: String?,
    val memo: String?,
    val sourceEventId: String,
    val occurredAt: String?,
    val processedAt: String?,
    val registeredAt: String,
    val timeStamp: String

    ) : ICsvExport {
    override fun toHeader(): List<String> = listOf(
        "tag_id",
        "item_type_id",
        "location_id",
        "winder_id",
        "device_id",
        "weight",
        "width",
        "length",
        "thickness",
        "lot_no",
        "occurrence_reason",
        "quantity",
        "memo",
        "source_event_id",
        "occurred_at",
        "processed_at",
        "registered_at"
    )

    override fun toRow(): List<String> = listOf(
        tagId.toString(),
        itemTypeId.toString(),
        locationId?.toString() ?: "",
        winderId?.toString() ?: "",
        deviceId,
        weight ?: "",
        width ?: "",
        length ?: "",
        thickness ?: "",
        lotNo ?: "",
        occurrenceReason ?: "",
        quantity ?: "",
        memo ?: "",
        sourceEventId,
        occurredAt ?: "",
        processedAt ?: "",
        registeredAt
    )

    override fun toCsvType(): String = CsvType.InboundResult.displayNameJp

    override fun toCsvName(): String = "inbound_result_${timeStamp}.csv"
}
