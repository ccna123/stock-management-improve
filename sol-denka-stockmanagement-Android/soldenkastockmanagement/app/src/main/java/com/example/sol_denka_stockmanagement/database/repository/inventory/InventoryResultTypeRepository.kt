package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultTypeModel
import com.example.sol_denka_stockmanagement.model.inventory.toEntity
import com.example.sol_denka_stockmanagement.model.inventory.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultTypeRepository @Inject constructor(
    private val dao: InventoryResultTypeDao
){
    fun get(): Flow<List<InventoryResultTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun getInventoryResultTypeIdByCode(inventoryResultCode: String) =
        dao.getInventoryResultTypeIdByCode(inventoryResultCode)

    suspend fun insert(model: InventoryResultTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: InventoryResultTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: InventoryResultTypeModel) = dao.delete(model.toEntity())

    suspend fun upsertAll(models: List<InventoryResultTypeModel>) =
        dao.upsertAll(models.map { it.toEntity() })
}