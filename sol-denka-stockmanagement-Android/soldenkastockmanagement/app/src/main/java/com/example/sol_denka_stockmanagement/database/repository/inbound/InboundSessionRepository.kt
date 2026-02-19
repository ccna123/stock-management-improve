package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.model.inbound.toEntity
import com.example.sol_denka_stockmanagement.model.inbound.toModel
import com.example.sol_denka_stockmanagement.model.session.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundSessionRepository @Inject constructor(
    private val dao: InboundSessionDao
) {
    fun get(): Flow<List<InboundSessionModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getExecutedAt(): List<SessionModel> = dao.getExecutedAt()
    suspend fun insert(model: InboundSessionModel) = dao.insert(model.toEntity())
    suspend fun update(model: InboundSessionModel) = dao.update(model.toEntity())
    suspend fun delete(model: InboundSessionModel) = dao.delete(model.toEntity())
}
