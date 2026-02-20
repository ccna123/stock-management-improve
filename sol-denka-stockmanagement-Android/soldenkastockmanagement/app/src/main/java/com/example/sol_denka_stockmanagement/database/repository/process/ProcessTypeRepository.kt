package com.example.sol_denka_stockmanagement.database.repository.process

import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.model.process.toEntity
import com.example.sol_denka_stockmanagement.model.process.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessTypeRepository @Inject constructor(
    private val dao: ProcessTypeDao
){

    fun get(): Flow<List<ProcessTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun countRecord() = dao.countRecord()

    suspend fun getIdByName(processTypeName: String) = dao.getIdByName(processTypeName)

    suspend fun insert(model: ProcessTypeModel) = dao.insert(model.toEntity())

    suspend fun update(model: ProcessTypeModel) = dao.update(model.toEntity())

    suspend fun delete(model: ProcessTypeModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<ProcessTypeModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}