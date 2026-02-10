package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity

data class InventorySessionModel(
    val inventorySessionId: Int = 0,
    val sourceSessionUuid: String,
    val locationId: Int,
    val memo: String?,
    val deviceId: String,
    val executedAt: String,
)

fun InventorySessionEntity.toModel() = InventorySessionModel(
    inventorySessionId = inventorySessionId,
    sourceSessionUuid = sourceSessionUuid,
    locationId = locationId,
    memo = memo,
    deviceId = deviceId,
    executedAt = executedAt
)

fun InventorySessionModel.toEntity() = InventorySessionEntity(
    inventorySessionId = inventorySessionId,
    sourceSessionUuid = sourceSessionUuid,
    locationId = locationId,
    memo = memo,
    deviceId = deviceId,
    executedAt = executedAt
)
