package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventoryResultTypeEntity
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryResultTypeModel


fun InventoryResultTypeEntity.toModel() = InventoryResultTypeModel(
    inventoryResultTypeId = inventoryResultTypeId,
    inventoryResultTypeCode = inventoryResultTypeCode,
    inventoryResultTypeName = inventoryResultTypeName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun InventoryResultTypeModel.toEntity() =
    InventoryResultTypeEntity(
        inventoryResultTypeId = inventoryResultTypeId,
        inventoryResultTypeCode = inventoryResultTypeCode,
        inventoryResultTypeName = inventoryResultTypeName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
