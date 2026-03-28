package com.example.sol_denka_stockmanagement.data.local.repository.inventory

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryDetailDao
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryDetailRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryDetailRepositoryImpl @Inject constructor(
    private val dao: InventoryDetailDao
) : IInventoryDetailRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)
    override suspend fun insert(model: InventoryDetailModel) = dao.insert(model.toEntity())
    override suspend fun update(model: InventoryDetailModel) = dao.update(model.toEntity())
    override suspend fun delete(model: InventoryDetailModel) = dao.delete(model.toEntity())
}