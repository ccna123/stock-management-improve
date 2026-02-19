package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventorySessionDao
import com.example.sol_denka_stockmanagement.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.model.inventory.toEntity
import com.example.sol_denka_stockmanagement.model.inventory.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventorySessionRepository @Inject constructor(
    private val dao: InventorySessionDao
) {
    fun get(): Flow<List<InventorySessionModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getExecutedAt(): List<String> = dao.getExecutedAt()
    suspend fun insert(model: InventorySessionModel) = dao.insert(model.toEntity())
    suspend fun update(model: InventorySessionModel) = dao.update(model.toEntity())
    suspend fun delete(model: InventorySessionModel) = dao.delete(model.toEntity())
}
