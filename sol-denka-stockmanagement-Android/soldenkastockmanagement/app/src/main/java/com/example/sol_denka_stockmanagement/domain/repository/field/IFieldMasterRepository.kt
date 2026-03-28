package com.example.sol_denka_stockmanagement.domain.repository.field

import com.example.sol_denka_stockmanagement.domain.model.field.FieldMasterModel
import kotlinx.coroutines.flow.Flow

interface IFieldMasterRepository {
    fun get(): Flow<List<FieldMasterModel>>
    suspend fun countRecord(): Int
    suspend fun insert(model: FieldMasterModel): Long
    suspend fun update(model: FieldMasterModel)
    suspend fun delete(model: FieldMasterModel)
    suspend fun upsertAll(models: List<FieldMasterModel>)
}