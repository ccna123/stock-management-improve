package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryDetailEntity

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
