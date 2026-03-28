package com.example.sol_denka_stockmanagement.domain.repository.csv

import com.example.sol_denka_stockmanagement.domain.model.csv.CsvTaskTypeModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface ICsvTaskTypeRepository {
    fun get(): Flow<List<CsvTaskTypeModel>>
    suspend fun countRecord(): Int
    suspend fun getIdByTaskCode(taskCode: String): Int
    suspend fun insert(model: CsvTaskTypeModel): Long
    suspend fun update(model: CsvTaskTypeModel)
    suspend fun delete(model: CsvTaskTypeModel)
    suspend fun upsertAll(models: List<CsvTaskTypeModel>)
}