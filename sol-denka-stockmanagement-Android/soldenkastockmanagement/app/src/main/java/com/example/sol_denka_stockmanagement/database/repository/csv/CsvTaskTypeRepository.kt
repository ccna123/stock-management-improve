package com.example.sol_denka_stockmanagement.database.repository.csv

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvTaskTypeRepository @Inject constructor(
    private val dao: CsvTaskTypeDao
) {
    fun get(): Flow<List<CsvTaskTypeEntity>> = dao.get()
    suspend fun insert(e: CsvTaskTypeEntity) = dao.insert(e)
    suspend fun update(e: CsvTaskTypeEntity) = dao.update(e)
    suspend fun delete(e: CsvTaskTypeEntity) = dao.delete(e)
}
