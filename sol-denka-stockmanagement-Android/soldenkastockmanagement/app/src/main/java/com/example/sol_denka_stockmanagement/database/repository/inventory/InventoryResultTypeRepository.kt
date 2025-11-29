package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultTypeRepository @Inject constructor(
    private val dao: InventoryResultTypeDao
) {
    fun get(): Flow<List<InventoryResultTypeEntity>> = dao.get()
    suspend fun insert(e: InventoryResultTypeEntity) = dao.insert(e)
    suspend fun update(e: InventoryResultTypeEntity) = dao.update(e)
    suspend fun delete(e: InventoryResultTypeEntity) = dao.delete(e)
}
