package com.example.sol_denka_stockmanagement.domain.repository.inventory

import com.example.sol_denka_stockmanagement.domain.model.inventory.InventorySessionModel
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

interface IInventorySessionRepository {
    fun get(): Flow<List<InventorySessionModel>>
    suspend fun getSession(): List<SessionModel>
    suspend fun insert(model: InventorySessionModel): Long
    suspend fun update(model: InventorySessionModel)
    suspend fun delete(model: InventorySessionModel)
}