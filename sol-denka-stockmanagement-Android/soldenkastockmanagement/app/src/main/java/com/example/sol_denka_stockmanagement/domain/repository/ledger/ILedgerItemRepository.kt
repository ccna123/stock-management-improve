package com.example.sol_denka_stockmanagement.domain.repository.ledger

import com.example.sol_denka_stockmanagement.domain.model.ledger.LedgerItemModel
import kotlinx.coroutines.flow.Flow

interface ILedgerItemRepository {
    fun get(): Flow<List<LedgerItemModel>>
    suspend fun countRecord(): Int
    fun getMappedTagIdsFlow(): Flow<List<Int>>
    suspend fun insert(model: LedgerItemModel): Long
    suspend fun update(model: LedgerItemModel)
    suspend fun delete(model: LedgerItemModel)
    suspend fun upsertAll(models: List<LedgerItemModel>)
}