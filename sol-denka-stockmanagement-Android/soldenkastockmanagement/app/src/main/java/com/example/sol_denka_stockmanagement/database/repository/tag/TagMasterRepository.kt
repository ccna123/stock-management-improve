package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.database.dao.tag.TagMasterDao
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class TagMasterRepository @Inject constructor(
    private val dao: TagMasterDao
) {

    fun get(): Flow<List<TagMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }

    suspend fun getFullInfo() = dao.getFullInfo()

    suspend fun getLedgerIdByTagId(tagId: Int): Int{
        return dao.getLedgerIdByTagId(tagId)
    }
    suspend fun insert(model: TagMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: TagMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagMasterModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<TagMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}