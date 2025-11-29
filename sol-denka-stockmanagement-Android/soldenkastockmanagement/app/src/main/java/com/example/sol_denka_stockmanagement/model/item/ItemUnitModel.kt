package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity

data class ItemUnitMasterModel(
    val itemUnitId: Int,
    val itemUnitCode: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemUnitMasterEntity.toModel() = ItemUnitMasterModel(
    itemUnitId, itemUnitCode, createdAt, updatedAt
)

fun ItemUnitMasterModel.toEntity() = ItemUnitMasterEntity(
    itemUnitId, itemUnitCode, createdAt, updatedAt
)
