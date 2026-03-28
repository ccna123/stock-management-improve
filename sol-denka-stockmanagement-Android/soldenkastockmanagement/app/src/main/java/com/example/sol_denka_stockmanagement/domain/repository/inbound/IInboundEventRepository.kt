package com.example.sol_denka_stockmanagement.domain.repository.inbound

import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel
import kotlinx.coroutines.flow.Flow

interface IInboundEventRepository {
    fun get(): Flow<List<InboundEventModel>>
    suspend fun getEventBySessionId(sessionId: Int): InboundEventModel?
    suspend fun insert(model: InboundEventModel): Long
    suspend fun update(model: InboundEventModel)
    suspend fun delete(model: InboundEventModel)
}