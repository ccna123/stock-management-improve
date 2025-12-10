package com.example.sol_denka_stockmanagement.model.field

import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity

data class FieldMasterModel(
    val fieldId: Int = 0,
    val fieldName: String,
    val dataType: DataType,
    val controlType: ControlType
)

fun FieldMasterEntity.toModel() = FieldMasterModel(
    fieldId = fieldId,
    fieldName = fieldName,
    dataType = dataType,
    controlType = controlType
)

fun FieldMasterModel.toEntity() = FieldMasterEntity(
    fieldId = fieldId,
    fieldName = fieldName,
    dataType = dataType,
    controlType = controlType
)
