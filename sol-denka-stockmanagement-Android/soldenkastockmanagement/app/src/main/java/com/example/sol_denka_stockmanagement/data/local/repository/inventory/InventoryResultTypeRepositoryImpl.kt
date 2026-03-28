package com.example.sol_denka_stockmanagement.data.local.repository.inventory

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryResultTypeModel
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryResultTypeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultTypeRepositoryImpl @Inject constructor(
    private val dao: InventoryResultTypeDao
) : IInventoryResultTypeRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getInventoryResultTypeIdByCode(inventoryResultCode: String) = dao.getInventoryResultTypeIdByCode(inventoryResultCode)
    override suspend fun insert(model: InventoryResultTypeModel) = dao.insert(model.toEntity())
    override suspend fun update(model: InventoryResultTypeModel) = dao.update(model.toEntity())
    override suspend fun delete(model: InventoryResultTypeModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<InventoryResultTypeModel>) = dao.upsertAll(models.map { it.toEntity() })
}