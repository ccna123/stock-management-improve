package com.example.sol_denka_stockmanagement.data.local.repository.outbound

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundSessionDao
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundSessionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundSessionRepositoryImpl @Inject constructor(
    private val dao: OutboundSessionDao
) : IOutboundSessionRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getSession() = dao.getSession()
    override suspend fun insert(model: OutboundSessionModel) = dao.insert(model.toEntity())
    override suspend fun update(model: OutboundSessionModel) = dao.update(model.toEntity())
    override suspend fun delete(model: OutboundSessionModel) = dao.delete(model.toEntity())
}