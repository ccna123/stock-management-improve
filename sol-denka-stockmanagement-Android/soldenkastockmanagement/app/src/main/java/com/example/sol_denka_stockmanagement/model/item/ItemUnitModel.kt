package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity

data class ItemUnitMasterModel(
    val itemUnitId: Int,
    val itemUnitCode: String,
    val unitCategory: Int,
    val itemUnitName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemUnitMasterEntity.toModel() = ItemUnitMasterModel(
    itemUnitId = itemUnitId,
    itemUnitCode = itemUnitCode,
    unitCategory = unitCategory,
    itemUnitName = itemUnitName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemUnitMasterModel.toEntity() = ItemUnitMasterEntity(
    itemUnitId = itemUnitId,
    itemUnitCode = itemUnitCode,
    unitCategory = unitCategory,
    itemUnitName = itemUnitName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
