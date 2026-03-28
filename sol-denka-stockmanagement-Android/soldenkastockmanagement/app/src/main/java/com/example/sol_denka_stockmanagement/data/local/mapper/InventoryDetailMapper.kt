package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventoryDetailEntity
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel

fun InventoryDetailEntity.toModel() = InventoryDetailModel(
    inventoryDetailId = inventoryDetailId,
    inventorySessionId = inventorySessionId,
    ledgerItemId = ledgerItemId,
    tagId = tagId,
    scannedAt = scannedAt
)

fun InventoryDetailModel.toEntity() =
    InventoryDetailEntity(
        inventoryDetailId = inventoryDetailId,
        inventorySessionId = inventorySessionId,
        ledgerItemId = ledgerItemId,
        tagId = tagId,
        scannedAt = scannedAt
    )
