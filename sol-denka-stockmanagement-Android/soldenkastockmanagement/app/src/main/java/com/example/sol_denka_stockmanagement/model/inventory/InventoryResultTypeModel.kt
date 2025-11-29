package com.example.sol_denka_stockmanagement.model.inventory

import com.example.sol_denka_stockmanagement.constant.InventoryResultCode
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity

data class InventoryResultTypeModel(
    val inventoryResultTypeId: Int,
    val inventoryResultCode: InventoryResultCode,
    val inventoryResultName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun InventoryResultTypeEntity.toModel() = InventoryResultTypeModel(
    inventoryResultTypeId, inventoryResultCode, inventoryResultName, createdAt, updatedAt
)

fun InventoryResultTypeModel.toEntity() = InventoryResultTypeEntity(
    inventoryResultTypeId, inventoryResultCode, inventoryResultName, createdAt, updatedAt
)
