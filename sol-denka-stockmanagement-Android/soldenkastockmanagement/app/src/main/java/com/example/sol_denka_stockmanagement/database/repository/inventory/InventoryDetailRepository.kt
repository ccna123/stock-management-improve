package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryDetailDao
import com.example.sol_denka_stockmanagement.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.model.inventory.toEntity
import com.example.sol_denka_stockmanagement.model.inventory.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryDetailRepository @Inject constructor(
    private val dao: InventoryDetailDao
) {
    fun get(): Flow<List<InventoryDetailModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId).toModel()
    suspend fun insert(model: InventoryDetailModel) = dao.insert(model.toEntity())
    suspend fun update(model: InventoryDetailModel) = dao.update(model.toEntity())
    suspend fun delete(model: InventoryDetailModel) = dao.delete(model.toEntity())
}
