package com.example.sol_denka_stockmanagement.domain.repository.inventory

import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryDetailModel
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryEventForExportModel
import kotlinx.coroutines.flow.Flow

interface IInventoryDetailRepository {
    fun get(): Flow<List<InventoryDetailModel>>
    suspend fun getEventBySessionId(sessionId: Int): List<InventoryEventForExportModel>
    suspend fun insert(model: InventoryDetailModel): Long
    suspend fun update(model: InventoryDetailModel)
    suspend fun delete(model: InventoryDetailModel)
}