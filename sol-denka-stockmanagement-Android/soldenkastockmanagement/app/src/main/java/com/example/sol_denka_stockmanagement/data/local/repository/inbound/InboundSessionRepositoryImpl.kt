package com.example.sol_denka_stockmanagement.data.local.repository.inbound

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundSessionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundSessionRepositoryImpl @Inject constructor(
    private val dao: InboundSessionDao
) : IInboundSessionRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getSession() = dao.getSession()
    override suspend fun insert(model: InboundSessionModel) = dao.insert(model.toEntity())
    override suspend fun update(model: InboundSessionModel) = dao.update(model.toEntity())
    override suspend fun delete(model: InboundSessionModel) = dao.delete(model.toEntity())
}