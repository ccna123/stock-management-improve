package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.model.inbound.toEntity
import com.example.sol_denka_stockmanagement.model.inbound.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundEventRepository @Inject constructor(
    private val dao: InboundEventDao
) {
    fun get(): Flow<List<InboundEventModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)?.toModel()
    suspend fun insert(model: InboundEventModel) = dao.insert(model.toEntity())
    suspend fun update(model: InboundEventModel) = dao.update(model.toEntity())
    suspend fun delete(model: InboundEventModel) = dao.delete(model.toEntity())
}
