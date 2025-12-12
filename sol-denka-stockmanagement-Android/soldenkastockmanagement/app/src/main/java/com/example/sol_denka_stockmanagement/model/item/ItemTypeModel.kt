package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity

data class ItemTypeMasterModel(
    val itemTypeId: Int,
    val itemUnitId: Int,
    val itemCategoryId: Int,
    val itemTypeCode: String,
    val itemTypeName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemTypeMasterEntity.toModel() = ItemTypeMasterModel(
    itemTypeId = itemTypeId,
    itemUnitId = itemUnitId,
    itemCategoryId = itemCategoryId,
    itemTypeCode = itemTypeCode,
    itemTypeName = itemTypeName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemTypeMasterModel.toEntity() = ItemTypeMasterEntity(
    itemTypeId = itemTypeId,
    itemUnitId = itemUnitId,
    itemCategoryId = itemCategoryId,
    itemTypeCode = itemTypeCode,
    itemTypeName = itemTypeName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
