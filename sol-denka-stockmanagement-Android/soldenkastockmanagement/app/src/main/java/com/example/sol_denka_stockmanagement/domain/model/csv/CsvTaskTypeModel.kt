package com.example.sol_denka_stockmanagement.domain.model.csv

import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity

data class CsvTaskTypeModel(
    val csvTaskTypeId: Int = 0,
    val csvTaskCode: String,
    val csvTaskName: String,
    val createdAt: String,
    val updatedAt: String
)