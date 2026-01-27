package com.example.sol_denka_stockmanagement.model.field

import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity

data class FieldMasterModel(
    val fieldId: Int = 0,
    val fieldName: String,
    val fieldCode: String,
    val dataType: String,
    val controlType: String
)

fun FieldMasterEntity.toModel() = FieldMasterModel(
    fieldId = fieldId,
    fieldName = fieldName,
    fieldCode = fieldCode,
    dataType = dataType,
    controlType = controlType
)

fun FieldMasterModel.toEntity() = FieldMasterEntity(
    fieldId = fieldId,
    fieldName = fieldName,
    fieldCode = fieldCode,
    dataType = dataType,
    controlType = controlType
)
