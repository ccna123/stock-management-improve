package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.item.ItemCategoryEntity
import com.example.sol_denka_stockmanagement.domain.model.item.ItemCategoryModel

fun ItemCategoryEntity.toModel() = ItemCategoryModel(
    itemCategoryId = itemCategoryId,
    itemCategoryName = itemCategoryName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemCategoryModel.toEntity() =
    ItemCategoryEntity(
        itemCategoryId = itemCategoryId,
        itemCategoryName = itemCategoryName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
