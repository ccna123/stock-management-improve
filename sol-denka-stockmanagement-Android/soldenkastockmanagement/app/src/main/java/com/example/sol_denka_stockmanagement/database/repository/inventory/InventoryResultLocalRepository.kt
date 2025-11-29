package com.example.sol_denka_stockmanagement.database.repository.inventory

import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultLocalDao
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultLocalModel
import com.example.sol_denka_stockmanagement.model.inventory.toEntity
import com.example.sol_denka_stockmanagement.model.inventory.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultLocalRepository @Inject constructor(
    private val dao: InventoryResultLocalDao
) {
    fun get(): Flow<List<InventoryResultLocalModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: InventoryResultLocalModel) = dao.insert(model.toEntity())
    suspend fun update(model: InventoryResultLocalModel) = dao.update(model.toEntity())
    suspend fun delete(model: InventoryResultLocalModel) = dao.delete(model.toEntity())
}
