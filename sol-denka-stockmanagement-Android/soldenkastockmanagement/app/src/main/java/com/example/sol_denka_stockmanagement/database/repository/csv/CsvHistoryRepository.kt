package com.example.sol_denka_stockmanagement.database.repository.csv

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.model.csv.CsvHistoryModel
import com.example.sol_denka_stockmanagement.model.csv.toEntity
import com.example.sol_denka_stockmanagement.model.csv.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvHistoryRepository @Inject constructor(
    private val dao: CsvHistoryDao
) {
    fun get(): Flow<List<CsvHistoryModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun countRecord() = dao.countRecord()
    suspend fun insert(model: CsvHistoryModel) = dao.insert(model.toEntity())
    suspend fun update(model: CsvHistoryModel) = dao.update(model.toEntity())
    suspend fun delete(model: CsvHistoryModel) = dao.delete(model.toEntity())
}
