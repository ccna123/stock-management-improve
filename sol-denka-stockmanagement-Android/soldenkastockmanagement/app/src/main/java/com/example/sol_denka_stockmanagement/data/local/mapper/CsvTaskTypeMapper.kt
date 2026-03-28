package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel


fun CsvTaskTypeEntity.toModel() = CsvTaskTypeModel(
    csvTaskTypeId = csvTaskTypeId,
    csvTaskCode = csvTaskCode,
    csvTaskName = csvTaskName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CsvTaskTypeModel.toEntity() =
    CsvTaskTypeEntity(
        csvTaskTypeId = csvTaskTypeId,
        csvTaskCode = csvTaskCode,
        csvTaskName = csvTaskName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
