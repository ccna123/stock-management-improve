package com.example.sol_denka_stockmanagement.domain.repository.outbound

import com.example.sol_denka_stockmanagement.domain.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundEventForExportModel
import kotlinx.coroutines.flow.Flow

interface IOutboundEventRepository {
    fun get(): Flow<List<OutBoundEventModel>>
    suspend fun getEventBySessionId(sessionId: Int): List<OutboundEventForExportModel>
    suspend fun insert(model: OutBoundEventModel): Long
    suspend fun update(model: OutBoundEventModel)
    suspend fun delete(model: OutBoundEventModel)
}