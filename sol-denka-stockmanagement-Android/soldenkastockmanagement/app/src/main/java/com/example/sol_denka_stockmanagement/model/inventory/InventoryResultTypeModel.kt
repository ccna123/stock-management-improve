package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity

data class InventoryResultTypeModel(
    val inventoryResultTypeId: Int,
    val inventoryResultTypeCode: InventoryResultType,
    val inventoryResultTypeName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun InventoryResultTypeEntity.toModel() = InventoryResultTypeModel(
    inventoryResultTypeId = inventoryResultTypeId,
    inventoryResultTypeCode = inventoryResultTypeCode,
    inventoryResultTypeName = inventoryResultTypeName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun InventoryResultTypeModel.toEntity() = InventoryResultTypeEntity(
    inventoryResultTypeId = inventoryResultTypeId,
    inventoryResultTypeCode = inventoryResultTypeCode,
    inventoryResultTypeName = inventoryResultTypeName,
    createdAt = createdAt,
    updatedAt = updatedAt
)