package com.example.sol_denka_stockmanagement.domain.repository.inbound

import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

interface IInboundSessionRepository {
    fun get(): Flow<List<InboundSessionModel>>
    suspend fun getSession(): List<SessionModel>
    suspend fun insert(model: InboundSessionModel): Long
    suspend fun update(model: InboundSessionModel)
    suspend fun delete(model: InboundSessionModel)
}