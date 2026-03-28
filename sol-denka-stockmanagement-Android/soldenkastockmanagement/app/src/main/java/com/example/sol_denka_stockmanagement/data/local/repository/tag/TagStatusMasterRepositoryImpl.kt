package com.example.sol_denka_stockmanagement.data.local.repository.tag

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.tag.TagStatusMasterDao
import com.example.sol_denka_stockmanagement.domain.model.tag.TagStatusMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagStatusMasterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagStatusMasterRepositoryImpl @Inject constructor(
    private val dao: TagStatusMasterDao
): ITagStatusMasterRepository {

    override fun get(): Flow<List<TagStatusMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }

    override suspend fun countRecord() = dao.countRecord()

    override suspend fun insert(model: TagStatusMasterModel) = dao.insert(model.toEntity())

    override suspend fun update(model: TagStatusMasterModel) = dao.update(model.toEntity())

    override suspend fun delete(model: TagStatusMasterModel) = dao.delete(model.toEntity())

    override suspend fun upsertAll(models: List<TagStatusMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}