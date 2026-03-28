package com.example.sol_denka_stockmanagement.domain.repository.tag

import com.example.sol_denka_stockmanagement.domain.model.tag.SingleTagInfoModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import kotlinx.coroutines.flow.Flow

interface ITagMasterRepository {
    fun get(): Flow<List<TagMasterModel>>
    suspend fun countRecord(): Int
    suspend fun getFullInfo(): List<SingleTagInfoModel>
    suspend fun getLedgerIdByTagId(tagId: Int): Int
    suspend fun insert(model: TagMasterModel): Long
    suspend fun update(model: TagMasterModel)
    suspend fun delete(model: TagMasterModel)
    suspend fun upsertAll(models: List<TagMasterModel>)
}