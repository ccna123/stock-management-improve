package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventorySessionDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventorySessionRepository @Inject constructor(
    private val dao: InventorySessionDao
) {
    fun get(): Flow<List<InventorySessionEntity>> = dao.get()
    suspend fun insert(e: InventorySessionEntity) = dao.insert(e)
    suspend fun update(e: InventorySessionEntity) = dao.update(e)
    suspend fun delete(e: InventorySessionEntity) = dao.delete(e)
}
