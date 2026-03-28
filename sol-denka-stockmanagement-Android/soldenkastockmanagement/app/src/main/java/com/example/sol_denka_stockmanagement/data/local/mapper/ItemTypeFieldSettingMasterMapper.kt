package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.field.ItemTypeFieldSettingMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel

fun ItemTypeFieldSettingMasterEntity.toModel() = ItemTypeFieldSettingMasterModel(
    itemTypeId = itemTypeId,
    fieldId = fieldId,
    isRequired = isRequired,
    isVisible = isVisible
)


fun ItemTypeFieldSettingMasterModel.toEntity() =
    ItemTypeFieldSettingMasterEntity(
        itemTypeId = itemTypeId,
        fieldId = fieldId,
        isRequired = isRequired,
        isVisible = isVisible
    )
