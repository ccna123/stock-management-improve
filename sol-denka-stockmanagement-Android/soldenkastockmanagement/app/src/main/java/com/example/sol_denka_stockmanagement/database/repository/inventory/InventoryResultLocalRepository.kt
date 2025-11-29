package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultLocalDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultLocalRepository @Inject constructor(
    private val dao: InventoryResultLocalDao
) {
    fun get(): Flow<List<InventoryResultLocalEntity>> = dao.get()
    suspend fun insert(e: InventoryResultLocalEntity) = dao.insert(e)
    suspend fun update(e: InventoryResultLocalEntity) = dao.update(e)
    suspend fun delete(e: InventoryResultLocalEntity) = dao.delete(e)
}
