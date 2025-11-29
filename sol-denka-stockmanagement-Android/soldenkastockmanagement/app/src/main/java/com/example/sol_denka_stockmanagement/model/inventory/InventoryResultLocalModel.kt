package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity

data class InventoryResultLocalModel(
    val inventoryResultId: Int,
    val inventorySessionId: Int,
    val inventoryResultTypeId: Int,
    val ledgerItemId: Int,
    val tagId: Int,
    val memo: String?,
    val scannedAt: String?,
)

fun InventoryResultLocalEntity.toModel() = InventoryResultLocalModel(
    inventoryResultId = inventoryResultId,
    inventorySessionId = inventorySessionId,
    inventoryResultTypeId = inventoryResultTypeId,
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    memo = memo,
    scannedAt = scannedAt
)

fun InventoryResultLocalModel.toEntity() = InventoryResultLocalEntity(
    inventoryResultId = inventoryResultId,
    inventorySessionId = inventorySessionId,
    inventoryResultTypeId = inventoryResultTypeId,
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    memo = memo,
    scannedAt = scannedAt
)
