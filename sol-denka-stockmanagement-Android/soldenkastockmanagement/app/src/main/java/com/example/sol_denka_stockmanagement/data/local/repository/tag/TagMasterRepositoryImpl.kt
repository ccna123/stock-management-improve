package com.example.sol_denka_stockmanagement.data.local.repository.tag

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.tag.TagMasterDao
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagMasterRepositoryImpl @Inject constructor(
    private val dao: TagMasterDao
) : ITagMasterRepository {

    override fun get(): Flow<List<TagMasterModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    override suspend fun countRecord() = dao.countRecord()

    override suspend fun getFullInfo() = dao.getFullInfo()

    override suspend fun getLedgerIdByTagId(tagId: Int): Int =
        dao.getLedgerIdByTagId(tagId)

    override suspend fun insert(model: TagMasterModel) = dao.insert(model.toEntity())
    override suspend fun update(model: TagMasterModel) = dao.update(model.toEntity())
    override suspend fun delete(model: TagMasterModel) = dao.delete(model.toEntity())

    override suspend fun upsertAll(models: List<TagMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}