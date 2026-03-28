package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.item.ItemUnitMasterModel

fun ItemUnitMasterEntity.toModel() = ItemUnitMasterModel(
    itemUnitId = itemUnitId,
    itemUnitCode = itemUnitCode,
    unitCategory = unitCategory,
    itemUnitName = itemUnitName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ItemUnitMasterModel.toEntity() =
    ItemUnitMasterEntity(
        itemUnitId = itemUnitId,
        itemUnitCode = itemUnitCode,
        unitCategory = unitCategory,
        itemUnitName = itemUnitName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
