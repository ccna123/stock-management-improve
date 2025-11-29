package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity

data class InventorySessionModel(
    val inventorySessionId: Int,
    val locationId: Int,
    val deviceId: String,
    val executedAt: String,
)

fun InventorySessionEntity.toModel() = InventorySessionModel(
    inventorySessionId, locationId, deviceId, executedAt
)

fun InventorySessionModel.toEntity() = InventorySessionEntity(
    inventorySessionId, locationId, deviceId, executedAt
)
