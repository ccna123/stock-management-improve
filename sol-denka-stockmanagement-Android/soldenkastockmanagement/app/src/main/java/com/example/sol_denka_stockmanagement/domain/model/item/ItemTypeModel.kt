package com.example.sol_denka_stockmanagement.domain.model.item

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