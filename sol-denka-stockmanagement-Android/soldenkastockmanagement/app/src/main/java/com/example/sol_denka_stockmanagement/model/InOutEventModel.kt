package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.database.entity.InOutEventEntity

data class InOutEventModel(
    val id: Int,
    val itemId: Int,
    val materialId: Int,
    val eventTypeId: Int,
    val performedLocationId: Int,
    val fromLocationId: Int,
    val toLocationId: Int,
    val epc: String,
    val isPresent: Int,
    val isExportCsv: Int,
    val memo: String,
    val occurredAt: String
)

fun InOutEventModel.toInoutEventEntity(): InOutEventEntity{
    return InOutEventEntity(
        id = id,
        itemId = itemId,
        materialId = materialId,
        eventTypeId = eventTypeId,
        performedLocationId = performedLocationId,
        fromLocationId = fromLocationId,
        toLocationId = toLocationId,
        epc = epc,
        isPresent = isPresent,
        isExportCsv = isExportCsv,
        memo = memo,
        occurredAt = occurredAt
    )
}

fun InOutEventEntity.toInOutEventModel(): InOutEventModel {
    return InOutEventModel(
        id = id,
        itemId = itemId ?: 0,
        materialId = materialId,
        eventTypeId = eventTypeId,
        performedLocationId = performedLocationId,
        fromLocationId = fromLocationId,
        toLocationId = toLocationId,
        epc = epc,
        isPresent = isPresent,
        isExportCsv = isExportCsv,
        memo = memo,
        occurredAt = occurredAt
    )
}
