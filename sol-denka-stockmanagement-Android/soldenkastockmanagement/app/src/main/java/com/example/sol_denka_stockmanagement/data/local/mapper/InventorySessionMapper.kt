package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel

class InventorySessionMapper {
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
}