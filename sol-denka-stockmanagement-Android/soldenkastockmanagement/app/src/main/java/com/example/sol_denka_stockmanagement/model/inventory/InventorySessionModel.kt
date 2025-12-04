package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity

data class InventorySessionModel(
    val inventorySessionId: Int = 0,
    val locationId: Int,
    val deviceId: String,
    val executedAt: String,
)

fun InventorySessionEntity.toModel() = InventorySessionModel(
    inventorySessionId = inventorySessionId,
    locationId = locationId,
    deviceId = deviceId,
    executedAt = executedAt
)

fun InventorySessionModel.toEntity() = InventorySessionEntity(
    inventorySessionId = inventorySessionId,
    locationId = locationId,
    deviceId = deviceId,
    executedAt = executedAt
)
