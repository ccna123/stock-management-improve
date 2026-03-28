package com.example.sol_denka_stockmanagement.database.repository.field

import com.example.sol_denka_stockmanagement.database.dao.field.ItemTypeFieldSettingMasterDao
import com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel
import com.example.sol_denka_stockmanagement.domain.model.field.toEntity
import com.example.sol_denka_stockmanagement.domain.model.field.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemTypeFieldSettingMasterRepository @Inject constructor(
    private val dao: ItemTypeFieldSettingMasterDao
) {

    fun get(): Flow<List<com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun countRecord() = dao.countRecord()
    suspend fun getFieldForItemTypeByItemTypeId(id: Int) = dao.getFieldForItemTypeByItemTypeId(id)
    suspend fun insert(model: com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel) = dao.delete(model.toEntity())
    suspend fun upsertAll(models: List<com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}
