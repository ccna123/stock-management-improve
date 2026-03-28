package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.domain.model.inbound.toEntity
import com.example.sol_denka_stockmanagement.domain.model.inbound.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundEventRepository @Inject constructor(
    private val dao: InboundEventDao
) {
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)?.toModel()
    suspend fun insert(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel) = dao.insert(model.toEntity())
    suspend fun update(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel) = dao.update(model.toEntity())
    suspend fun delete(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel) = dao.delete(model.toEntity())
}
