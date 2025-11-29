package com.example.sol_denka_stockmanagement.database.repository.outbound

import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundEventDao
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundEventRepository @Inject constructor(
    private val dao: OutboundEventDao
) {
    fun get(): Flow<List<OutBoundEventEntity>> = dao.get()
    suspend fun insert(e: OutBoundEventEntity) = dao.insert(e)
    suspend fun update(e: OutBoundEventEntity) = dao.update(e)
    suspend fun delete(e: OutBoundEventEntity) = dao.delete(e)
}
