package com.example.sol_denka_stockmanagement.database.repository.csv

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.model.csv.toEntity
import com.example.sol_denka_stockmanagement.model.csv.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvTaskTypeRepository @Inject constructor(
    private val dao: CsvTaskTypeDao
) {
    fun get(): Flow<List<CsvTaskTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: CsvTaskTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: CsvTaskTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: CsvTaskTypeModel) = dao.delete(model.toEntity())
}
