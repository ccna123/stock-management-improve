package com.example.sol_denka_stockmanagement.domain.model.item

data class ItemUnitMasterModel(
    val itemUnitId: Int,
    val itemUnitCode: String,
    val unitCategory: Int,
    val itemUnitName: String,
    val createdAt: String,
    val updatedAt: String,
)