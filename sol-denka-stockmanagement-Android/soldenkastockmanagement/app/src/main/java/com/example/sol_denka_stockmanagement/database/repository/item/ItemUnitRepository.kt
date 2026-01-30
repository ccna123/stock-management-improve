package com.example.sol_denka_stockmanagement.database.repository.item

import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.model.item.ItemUnitMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemUnitRepository @Inject constructor(
    private val dao: ItemUnitDao
){
    fun get(): Flow<List<ItemUnitMasterModel>> =
        dao.get().map { entityList -> entityList.map { it.toModel() } }

    suspend fun insert(model: ItemUnitMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: ItemUnitMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemUnitMasterModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<ItemUnitMasterModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}