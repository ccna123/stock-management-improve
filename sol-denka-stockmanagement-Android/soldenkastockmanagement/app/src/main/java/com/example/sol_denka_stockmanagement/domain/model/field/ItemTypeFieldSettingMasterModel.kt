package com.example.sol_denka_stockmanagement.domain.model.field

data class ItemTypeFieldSettingMasterModel(
    val itemTypeId: Int = 0,
    val fieldId: Int,
    val isRequired: Boolean,
    val isVisible: Boolean
)