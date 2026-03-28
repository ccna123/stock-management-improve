package com.example.sol_denka_stockmanagement.data.local.repository.inventory

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventorySessionDao
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventorySessionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventorySessionRepositoryImpl @Inject constructor(
    private val dao: InventorySessionDao
) : IInventorySessionRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getSession() = dao.getSession()
    override suspend fun insert(model: InventorySessionModel) = dao.insert(model.toEntity())
    override suspend fun update(model: InventorySessionModel) = dao.update(model.toEntity())
    override suspend fun delete(model: InventorySessionModel) = dao.delete(model.toEntity())
}