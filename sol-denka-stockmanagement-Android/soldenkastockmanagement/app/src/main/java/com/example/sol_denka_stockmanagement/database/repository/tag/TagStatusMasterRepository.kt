package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.database.dao.tag.TagStatusMasterDao
import com.example.sol_denka_stockmanagement.model.tag.TagStatusMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagStatusMasterRepository @Inject constructor(
    private val dao: TagStatusMasterDao
): IPresetRepo {

    override suspend fun ensurePresetInserted() {
    }

    fun get(): Flow<List<TagStatusMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }
    suspend fun insert(model: TagStatusMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: TagStatusMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagStatusMasterModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<TagStatusMasterModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}