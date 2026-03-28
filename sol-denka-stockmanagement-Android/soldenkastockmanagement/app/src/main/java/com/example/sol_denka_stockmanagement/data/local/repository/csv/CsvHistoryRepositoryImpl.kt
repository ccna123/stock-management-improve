package com.example.sol_denka_stockmanagement.data.local.repository.csv

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvHistoryModel
import com.example.sol_denka_stockmanagement.domain.repository.csv.ICsvHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvHistoryRepositoryImpl @Inject constructor(
    private val dao: CsvHistoryDao
): ICsvHistoryRepository {

    override fun get(): Flow<List<CsvHistoryModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun insert(model: CsvHistoryModel) = dao.insert(model.toEntity())
    override suspend fun update(model: CsvHistoryModel) = dao.update(model.toEntity())
    override suspend fun delete(model: CsvHistoryModel) = dao.delete(model.toEntity())
}
