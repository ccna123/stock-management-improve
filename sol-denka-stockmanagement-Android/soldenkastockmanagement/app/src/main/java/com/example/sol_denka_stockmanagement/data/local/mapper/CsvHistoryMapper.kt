package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvHistoryModel

fun CsvHistoryEntity.toModel() =
    CsvHistoryModel(
        csvHistoryId = csvHistoryId,
        csvTaskTypeId = csvTaskTypeId,
        fileName = fileName,
        direction = direction,
        result = result,
        recordNum = recordNum,
        errorMessage = errorMessage,
        executedAt = executedAt
    )

fun CsvHistoryModel.toEntity() =
    CsvHistoryEntity(
        csvHistoryId = csvHistoryId,
        csvTaskTypeId = csvTaskTypeId,
        fileName = fileName,
        direction = direction,
        result = result,
        recordNum = recordNum,
        errorMessage = errorMessage,
        executedAt = executedAt
    )
