package com.example.sol_denka_stockmanagement.database.repository.outbound

import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundSessionDao
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundSessionRepository @Inject constructor(
    private val dao: OutboundSessionDao
) {
    fun get(): Flow<List<OutboundSessionEntity>> = dao.get()
    suspend fun insert(e: OutboundSessionEntity) = dao.insert(e)
    suspend fun update(e: OutboundSessionEntity) = dao.update(e)
    suspend fun delete(e: OutboundSessionEntity) = dao.delete(e)
}
