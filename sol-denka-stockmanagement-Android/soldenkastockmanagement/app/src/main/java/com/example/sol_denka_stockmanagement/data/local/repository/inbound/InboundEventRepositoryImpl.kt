package com.example.sol_denka_stockmanagement.data.local.repository.inbound

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundEventRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundEventRepositoryImpl @Inject constructor(
    private val dao: InboundEventDao
) : IInboundEventRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)?.toModel()
    override suspend fun insert(model: InboundEventModel) = dao.insert(model.toEntity())
    override suspend fun update(model: InboundEventModel) = dao.update(model.toEntity())
    override suspend fun delete(model: InboundEventModel) = dao.delete(model.toEntity())
}