package com.example.sol_denka_stockmanagement.database.repository.field

import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.model.field.FieldMasterModel
import com.example.sol_denka_stockmanagement.model.field.toEntity
import com.example.sol_denka_stockmanagement.model.field.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldMasterRepository @Inject constructor(
    private val dao: FieldMasterDao
){
    fun get(): Flow<List<FieldMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun countRecord() = dao.countRecord()
    suspend fun insert(model: FieldMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: FieldMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: FieldMasterModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<FieldMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}