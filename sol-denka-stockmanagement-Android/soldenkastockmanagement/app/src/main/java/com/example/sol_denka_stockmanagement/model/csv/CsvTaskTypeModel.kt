package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity

data class CsvTaskTypeModel(
    val csvTaskTypeId: Int,
    val csvTaskCode: String,
    val csvTaskName: String,
    val createdAt: String,
    val updatedAt: String
)

fun CsvTaskTypeEntity.toModel() = CsvTaskTypeModel(
    csvTaskTypeId = csvTaskTypeId,
    csvTaskCode = csvTaskCode,
    csvTaskName = csvTaskName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CsvTaskTypeModel.toEntity() = CsvTaskTypeEntity(
    csvTaskTypeId = csvTaskTypeId,
    csvTaskCode = csvTaskCode,
    csvTaskName = csvTaskName,
    createdAt = createdAt,
    updatedAt = updatedAt
)


