package com.example.sol_denka_stockmanagement.database.repository.field

import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel
import com.example.sol_denka_stockmanagement.domain.model.field.toEntity
import com.example.sol_denka_stockmanagement.domain.model.field.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldMasterRepository @Inject constructor(
    private val dao: FieldMasterDao
){
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun countRecord() = dao.countRecord()
    suspend fun insert(model: com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}