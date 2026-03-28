package com.example.sol_denka_stockmanagement.data.local.repository.field

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.field.IFieldMasterRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldMasterRepositoryImpl @Inject constructor(
    private val dao: FieldMasterDao
) : IFieldMasterRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun insert(model: FieldMasterModel) = dao.insert(model.toEntity())
    override suspend fun update(model: FieldMasterModel) = dao.update(model.toEntity())
    override suspend fun delete(model: FieldMasterModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<FieldMasterModel>) = dao.upsertAll(models.map { it.toEntity() })
}