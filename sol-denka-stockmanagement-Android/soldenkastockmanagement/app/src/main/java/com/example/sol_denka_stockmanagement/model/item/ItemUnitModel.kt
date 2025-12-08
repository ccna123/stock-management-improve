package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity

data class ItemUnitMasterModel(
    val itemUnitId: Int = 0,
    val itemUnitCode: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemUnitMasterEntity.toModel() = ItemUnitMasterModel(
    itemUnitId = itemUnitId,
    itemUnitCode = itemUnitCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemUnitMasterModel.toEntity() = ItemUnitMasterEntity(
    itemUnitId = itemUnitId,
    itemUnitCode = itemUnitCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)
