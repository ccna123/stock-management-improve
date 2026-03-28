package com.example.sol_denka_stockmanagement.database.repository.csv

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.domain.model.csv.toEntity
import com.example.sol_denka_stockmanagement.domain.model.csv.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvTaskTypeRepository @Inject constructor(
    private val dao: CsvTaskTypeDao
){
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun countRecord() = dao.countRecord()
    suspend fun getIdByTaskCode(taskCode: String) = dao.getIdByTaskCode(taskCode)
    suspend fun insert(model: com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel) = dao.delete(model.toEntity())
    suspend fun upsertAll(models: List<com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}