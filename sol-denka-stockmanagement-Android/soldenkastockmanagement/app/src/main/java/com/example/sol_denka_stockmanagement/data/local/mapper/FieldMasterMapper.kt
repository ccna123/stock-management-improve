package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel

class FieldMasterMapper {
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
}