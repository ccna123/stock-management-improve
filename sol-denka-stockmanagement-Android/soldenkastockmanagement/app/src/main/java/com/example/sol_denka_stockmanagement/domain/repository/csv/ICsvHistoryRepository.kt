package com.example.sol_denka_stockmanagement.domain.repository.csv

import com.example.sol_denka_stockmanagement.domain.model.csv.CsvHistoryModel
import kotlinx.coroutines.flow.Flow

interface ICsvHistoryRepository {
    fun get(): Flow<List<CsvHistoryModel>>
    suspend fun countRecord(): Int
    suspend fun insert(model: CsvHistoryModel): Long
    suspend fun update(model: CsvHistoryModel)
    suspend fun delete(model: CsvHistoryModel)
}
