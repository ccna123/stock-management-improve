package com.example.sol_denka_stockmanagement.domain.repository.outbound

import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

interface IOutboundSessionRepository {
    fun get(): Flow<List<OutboundSessionModel>>
    suspend fun getSession(): List<SessionModel>
    suspend fun insert(model: OutboundSessionModel): Long
    suspend fun update(model: OutboundSessionModel)
    suspend fun delete(model: OutboundSessionModel)
}