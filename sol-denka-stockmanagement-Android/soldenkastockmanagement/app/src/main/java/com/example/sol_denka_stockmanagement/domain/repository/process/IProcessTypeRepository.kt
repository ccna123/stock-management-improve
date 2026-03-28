package com.example.sol_denka_stockmanagement.domain.repository.process

import com.example.sol_denka_stockmanagement.domain.model.process.ProcessTypeModel
import kotlinx.coroutines.flow.Flow

interface IProcessTypeRepository {
    fun get(): Flow<List<ProcessTypeModel>>
    suspend fun countRecord(): Int
    suspend fun getIdByName(processTypeName: String): Int
    suspend fun insert(model: ProcessTypeModel): Long
    suspend fun update(model: ProcessTypeModel)
    suspend fun delete(model: ProcessTypeModel)
    suspend fun upsertAll(models: List<ProcessTypeModel>)
}