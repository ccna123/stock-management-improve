package com.example.sol_denka_stockmanagement.domain.repository.tag

import com.example.sol_denka_stockmanagement.domain.model.tag.TagStatusMasterModel
import kotlinx.coroutines.flow.Flow

interface ITagStatusMasterRepository {
    fun get(): Flow<List<TagStatusMasterModel>>
    suspend fun countRecord(): Int
    suspend fun insert(model: TagStatusMasterModel): Long
    suspend fun update(model: TagStatusMasterModel)
    suspend fun delete(model: TagStatusMasterModel)
    suspend fun upsertAll(models: List<TagStatusMasterModel>)
}