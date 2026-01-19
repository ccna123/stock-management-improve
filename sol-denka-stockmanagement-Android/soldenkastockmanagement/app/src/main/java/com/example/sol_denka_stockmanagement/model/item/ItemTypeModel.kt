package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import java.math.BigDecimal

data class ItemTypeMasterModel(
    val itemTypeId: Int,
    val itemCountUnitId: Int?,
    val itemWeightUnitId: Int?,
    val itemCategoryId: Int,
    val itemTypeCode: String?,
    val itemTypeName: String,
    val packingType: String?,
    val grade: String?,
    val specificGravity: BigDecimal?,
    val memo: String?,
    val unitWeight: Long?,
)

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

fun ItemTypeMasterModel.toEntity() = ItemTypeMasterEntity(
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
