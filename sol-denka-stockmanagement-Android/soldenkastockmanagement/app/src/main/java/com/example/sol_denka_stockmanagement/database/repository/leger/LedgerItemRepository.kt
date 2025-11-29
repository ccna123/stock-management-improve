package com.example.sol_denka_stockmanagement.database.repository.leger

import com.example.sol_denka_stockmanagement.database.dao.leger.LedgerItemDao
import com.example.sol_denka_stockmanagement.database.entity.leger.LedgerItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LedgerItemRepository @Inject constructor(
    private val dao: LedgerItemDao
) {
    fun get(): Flow<List<LedgerItemEntity>> = dao.get()
    suspend fun insert(e: LedgerItemEntity) = dao.insert(e)
    suspend fun update(e: LedgerItemEntity) = dao.update(e)
    suspend fun delete(e: LedgerItemEntity) = dao.delete(e)
}
