package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.item.ItemTypeMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.item.ItemTypeMasterModel

fun ItemTypeMasterEntity.toModel() = ItemTypeMasterModel(
    itemTypeId = itemTypeId,
    itemCountUnitId = itemCountUnitId,
    itemWeightUnitId = itemWeightUnitId,
    itemCategoryId = itemCategoryId,
    itemTypeCode = itemTypeCode,
    itemTypeName = itemTypeName,
    packingType = packingType,
    specificGravity = specificGravity,
    grade = grade,
    memo = memo,
    unitWeight = unitWeight
)

fun ItemTypeMasterModel.toEntity() =
    ItemTypeMasterEntity(
        itemTypeId = itemTypeId,
        itemCountUnitId = itemCountUnitId,
        itemWeightUnitId = itemWeightUnitId,
        itemCategoryId = itemCategoryId,
        itemTypeCode = itemTypeCode,
        itemTypeName = itemTypeName,
        packingType = packingType,
        specificGravity = specificGravity,
        grade = grade,
        memo = memo,
        unitWeight = unitWeight
    )
