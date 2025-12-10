package com.example.sol_denka_stockmanagement.model.field

import com.example.sol_denka_stockmanagement.database.entity.field.ItemTypeFieldSettingMasterEntity

data class ItemTypeFieldSettingMasterModel(
    val itemTypeId: Int = 0,
    val fieldId: Int,
    val isRequired: Boolean,
    val isVisible: Boolean
)

fun ItemTypeFieldSettingMasterEntity.toModel() = ItemTypeFieldSettingMasterModel(
    itemTypeId = itemTypeId,
    fieldId = fieldId,
    isRequired = isRequired,
    isVisible = isVisible
)


fun ItemTypeFieldSettingMasterModel.toEntity() = ItemTypeFieldSettingMasterEntity(
    itemTypeId = itemTypeId,
    fieldId = fieldId,
    isRequired = isRequired,
    isVisible = isVisible
)

