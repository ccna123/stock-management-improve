package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundSessionRepository @Inject constructor(
    private val dao: InboundSessionDao
) {
    fun get(): Flow<List<InboundSessionEntity>> = dao.get()
    suspend fun insert(e: InboundSessionEntity) = dao.insert(e)
    suspend fun update(e: InboundSessionEntity) = dao.update(e)
    suspend fun delete(e: InboundSessionEntity) = dao.delete(e)
}
