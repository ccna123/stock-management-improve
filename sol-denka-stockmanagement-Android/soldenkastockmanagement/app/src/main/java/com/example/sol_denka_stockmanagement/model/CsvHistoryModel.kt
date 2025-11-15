package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.constant.CsvHistoryTarget
import com.example.sol_denka_stockmanagement.database.entity.CsvHistoryEntity

data class CsvHistoryModel(
    val id: Int,
    val fileName: String,
    val direction: CsvHistoryDirection,
    val target: CsvHistoryTarget,
    val result: CsvHistoryResult,
    val executedAt: String
)

fun CsvHistoryModel.toCsvHistoryEntity(): CsvHistoryEntity {
    return CsvHistoryEntity(
        id = this.id,
        fileName = this.fileName,
        direction = this.direction,
        target = this.target,
        result = this.result,
        executedAt = this.executedAt
    )
}

fun CsvHistoryEntity.toCsvHistoryModel(): CsvHistoryModel{
    return CsvHistoryModel(
        id = this.id,
        fileName = this.fileName,
        direction = this.direction,
        target = this.target,
        result = this.result,
        executedAt = this.executedAt
    )
}
