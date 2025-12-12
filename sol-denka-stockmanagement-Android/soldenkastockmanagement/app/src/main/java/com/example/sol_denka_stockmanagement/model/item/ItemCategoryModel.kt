package com.example.sol_denka_stockmanagement.model.item

import com.example.sol_denka_stockmanagement.database.entity.item.ItemCategoryEntity

data class ItemCategoryModel(
    val itemCategoryId: Int,
    val itemCategoryName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ItemCategoryEntity.toModel() = ItemCategoryModel(
    itemCategoryId = itemCategoryId,
    itemCategoryName = itemCategoryName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemCategoryModel.toEntity() = ItemCategoryEntity(
    itemCategoryId = itemCategoryId,
    itemCategoryName = itemCategoryName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
