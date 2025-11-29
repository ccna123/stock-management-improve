package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundEventRepository @Inject constructor(
    private val dao: InboundEventDao
) {
    fun get(): Flow<List<InboundEventEntity>> = dao.get()
    suspend fun insert(e: InboundEventEntity) = dao.insert(e)
    suspend fun update(e: InboundEventEntity) = dao.update(e)
    suspend fun delete(e: InboundEventEntity) = dao.delete(e)
}
