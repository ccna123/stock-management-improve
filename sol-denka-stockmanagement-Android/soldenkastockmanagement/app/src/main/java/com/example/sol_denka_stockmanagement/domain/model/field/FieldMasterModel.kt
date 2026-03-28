package com.example.sol_denka_stockmanagement.domain.model.field

import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity

data class FieldMasterModel(
    val fieldId: Int = 0,
    val fieldName: String,
    val fieldCode: String,
    val dataType: String,
    val controlType: String
)
