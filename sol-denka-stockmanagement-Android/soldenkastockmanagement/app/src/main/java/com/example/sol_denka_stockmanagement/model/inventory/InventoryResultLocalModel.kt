package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity

data class InventoryResultLocalModel(
    val inventoryResultId: Int,
    val inventorySessionId: Int,
    val inventoryResultType: Int,
    val ledgerItemId: Int,
    val tagId: Int,
    val memo: String?,
    val scannedAt: String?,
)

fun InventoryResultLocalEntity.toModel() = InventoryResultLocalModel(
    inventoryResultId, inventorySessionId, inventoryResultType,
    ledgerItemId, tagId, memo, scannedAt
)

fun InventoryResultLocalModel.toEntity() = InventoryResultLocalEntity(
    inventoryResultId, inventorySessionId, inventoryResultType,
    ledgerItemId, tagId, memo, scannedAt
)
