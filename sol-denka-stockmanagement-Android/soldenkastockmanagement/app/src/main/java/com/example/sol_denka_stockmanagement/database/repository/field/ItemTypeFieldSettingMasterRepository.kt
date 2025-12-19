package com.example.sol_denka_stockmanagement.database.repository.field

import com.example.sol_denka_stockmanagement.database.dao.field.ItemTypeFieldSettingMasterDao
import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel
import com.example.sol_denka_stockmanagement.model.field.toEntity
import com.example.sol_denka_stockmanagement.model.field.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class ItemTypeFieldSettingMasterRepository @Inject constructor(
    private val dao: ItemTypeFieldSettingMasterDao
) {

    fun get(): Flow<List<ItemTypeFieldSettingMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getFieldForItemTypeByItemTypeId(id: Int) = dao.getFieldForItemTypeByItemTypeId(id)
    suspend fun insert(model: ItemTypeFieldSettingMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: ItemTypeFieldSettingMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemTypeFieldSettingMasterModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<ItemTypeFieldSettingMasterModel>) = dao.replaceAll(models.map { it.toEntity() })
}
