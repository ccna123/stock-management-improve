package com.example.sol_denka_stockmanagement.database.repository.csv

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvHistoryRepository @Inject constructor(
    private val dao: CsvHistoryDao
) {
    fun get(): Flow<List<CsvHistoryEntity>> = dao.get()
    suspend fun insert(e: CsvHistoryEntity) = dao.insert(e)
    suspend fun update(e: CsvHistoryEntity) = dao.update(e)
    suspend fun delete(e: CsvHistoryEntity) = dao.delete(e)
}
