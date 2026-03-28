package com.example.sol_denka_stockmanagement.data.local.repository.csv

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.domain.repository.csv.ICsvTaskTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvTaskTypeRepositoryImpl @Inject constructor(
    private val dao: CsvTaskTypeDao
): ICsvTaskTypeRepository {
    override fun get(): Flow<List<CsvTaskTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    override suspend fun countRecord() = dao.countRecord()
    override suspend fun getIdByTaskCode(taskCode: String) = dao.getIdByTaskCode(taskCode)
    override suspend fun insert(model: CsvTaskTypeModel) = dao.insert(model.toEntity())
    override suspend fun update(model: CsvTaskTypeModel) = dao.update(model.toEntity())
    override suspend fun delete(model: CsvTaskTypeModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<CsvTaskTypeModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}