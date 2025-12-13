package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity

data class ItemTypeMasterModel(
    val itemTypeId: Int,
    val itemUnitId: Int,
    val itemCategoryId: Int,
    val itemTypeCode: String,
    val itemTypeName: String,
    val packingType: String?,
    val grade: String?,
    val specificGravity: String?,
)

fun ItemTypeMasterEntity.toModel() = ItemTypeMasterModel(
    itemTypeId = itemTypeId,
    itemUnitId = itemUnitId,
    itemCategoryId = itemCategoryId,
    itemTypeCode = itemTypeCode,
    itemTypeName = itemTypeName,
    packingType = packingType,
    specificGravity = specificGravity,
    grade = grade,
)

fun ItemTypeMasterModel.toEntity() = ItemTypeMasterEntity(
    itemTypeId = itemTypeId,
    itemUnitId = itemUnitId,
    itemCategoryId = itemCategoryId,
    itemTypeCode = itemTypeCode,
    itemTypeName = itemTypeName,
    packingType = packingType,
    specificGravity = specificGravity,
    grade = grade,
)
