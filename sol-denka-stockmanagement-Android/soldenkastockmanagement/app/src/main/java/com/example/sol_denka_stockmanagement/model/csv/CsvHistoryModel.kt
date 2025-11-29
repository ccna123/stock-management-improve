package com.example.sol_denka_stockmanagement.model.csv

import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity

data class CsvHistoryModel(
    val csvHistoryId: Int,
    val csvTaskTypeId: Int,
    val fileName: String,
    val direction: CsvHistoryDirection,
    val result: CsvHistoryResult,
    val recordNum: Int?,
    val errorMessage: String?,
    val executedAt: String,
)

fun CsvHistoryEntity.toModel() = CsvHistoryModel(
    csvHistoryId, csvTaskTypeId, fileName, direction, result,
    recordNum, errorMessage, executedAt
)

fun CsvHistoryModel.toEntity() = CsvHistoryEntity(
    csvHistoryId, csvTaskTypeId, fileName, direction, result,
    recordNum, errorMessage, executedAt
)
