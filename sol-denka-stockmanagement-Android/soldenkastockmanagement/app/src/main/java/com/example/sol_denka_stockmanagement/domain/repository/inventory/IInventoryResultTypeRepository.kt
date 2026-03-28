package com.example.sol_denka_stockmanagement.domain.repository.inventory

import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryResultTypeModel
import kotlinx.coroutines.flow.Flow

interface IInventoryResultTypeRepository {
    fun get(): Flow<List<InventoryResultTypeModel>>
    suspend fun getInventoryResultTypeIdByCode(inventoryResultCode: String): Int
    suspend fun insert(model: InventoryResultTypeModel): Long
    suspend fun update(model: InventoryResultTypeModel)
    suspend fun delete(model: InventoryResultTypeModel)
    suspend fun upsertAll(models: List<InventoryResultTypeModel>)
}