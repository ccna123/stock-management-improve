package com.example.sol_denka_stockmanagement.database.repository.outbound

import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundEventDao
import com.example.sol_denka_stockmanagement.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.model.outbound.toEntity
import com.example.sol_denka_stockmanagement.model.outbound.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundEventRepository @Inject constructor(
    private val dao: OutboundEventDao
) {
    fun get(): Flow<List<OutBoundEventModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId).toModel()
    suspend fun insert(model: OutBoundEventModel) = dao.insert(model.toEntity())
    suspend fun update(model: OutBoundEventModel) = dao.update(model.toEntity())
    suspend fun delete(model: OutBoundEventModel) = dao.delete(model.toEntity())
}
