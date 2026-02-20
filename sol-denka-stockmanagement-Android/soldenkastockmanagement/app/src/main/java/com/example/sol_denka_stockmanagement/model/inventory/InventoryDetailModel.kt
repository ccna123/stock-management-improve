package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryDetailEntity
import com.example.sol_denka_stockmanagement.model.csv.InventoryResultCsvModel

data class InventoryDetailModel(
    val inventoryDetailId: Int = 0,
    val inventorySessionId: Int,
    val ledgerItemId: Int,
    val tagId: Int,
    val scannedAt: String,
)

fun InventoryDetailEntity.toModel() = InventoryDetailModel(
    inventoryDetailId = inventoryDetailId,
    inventorySessionId = inventorySessionId,
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    scannedAt = scannedAt
)

fun InventoryDetailModel.toEntity() = InventoryDetailEntity(
    inventoryDetailId = inventoryDetailId,
    inventorySessionId = inventorySessionId,
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    scannedAt = scannedAt
)

fun InventoryDetailModel.toCsvModel(
    sourceSessionId: String,
    memo: String,
    locationId: Int,
    deviceId: String,
    timeStamp: String,
    completedAt: String
) = InventoryResultCsvModel(
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    scannedAt = scannedAt,
    sourceSessionId = sourceSessionId,
    locationId = locationId,
    deviceId = deviceId,
    memo = memo,
    completedAt = completedAt,
    timeStamp = timeStamp
)
