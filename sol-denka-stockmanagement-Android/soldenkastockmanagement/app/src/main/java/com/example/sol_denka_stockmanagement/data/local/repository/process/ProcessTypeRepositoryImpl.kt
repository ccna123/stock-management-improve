package com.example.sol_denka_stockmanagement.data.local.repository.process

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.domain.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessTypeRepositoryImpl @Inject constructor(
    private val dao: ProcessTypeDao
) : IProcessTypeRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun getIdByName(processTypeName: String) = dao.getIdByName(processTypeName)
    override suspend fun insert(model: ProcessTypeModel) = dao.insert(model.toEntity())
    override suspend fun update(model: ProcessTypeModel) = dao.update(model.toEntity())
    override suspend fun delete(model: ProcessTypeModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<ProcessTypeModel>) = dao.upsertAll(models.map { it.toEntity() })
}