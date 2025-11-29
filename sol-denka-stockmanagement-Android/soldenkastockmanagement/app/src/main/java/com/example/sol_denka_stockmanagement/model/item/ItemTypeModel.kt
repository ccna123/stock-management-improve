package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity

data class ItemTypeMasterModel(
    val itemTypeId: Int,
    val itemTypeCode: String,
    val itemTypeName: String,
    val itemUnitId: Int,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemTypeMasterEntity.toModel() = ItemTypeMasterModel(
    itemTypeId, itemTypeCode, itemTypeName,
    itemUnitId, createdAt, updatedAt
)

fun ItemTypeMasterModel.toEntity() = ItemTypeMasterEntity(
    itemTypeId, itemTypeCode, itemTypeName,
    itemUnitId, createdAt, updatedAt
)
