package com.example.sol_denka_stockmanagement.data.local.repository.outbound

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundEventDao
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundEventRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundEventRepositoryImpl @Inject constructor(
    private val dao: OutboundEventDao
) : IOutboundEventRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)
    override suspend fun insert(model: OutBoundEventModel) = dao.insert(model.toEntity())
    override suspend fun update(model: OutBoundEventModel) = dao.update(model.toEntity())
    override suspend fun delete(model: OutBoundEventModel) = dao.delete(model.toEntity())
}