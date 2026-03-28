package com.example.sol_denka_stockmanagement.database.repository.inbound

import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.inbound.toEntity
import com.example.sol_denka_stockmanagement.domain.model.inbound.toModel
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundSessionRepository @Inject constructor(
    private val dao: InboundSessionDao
) {
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getSession(): List<com.example.sol_denka_stockmanagement.domain.model.session.SessionModel> = dao.getSession()
    suspend fun insert(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel) = dao.insert(model.toEntity())
    suspend fun update(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel) = dao.update(model.toEntity())
    suspend fun delete(model: com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel) = dao.delete(model.toEntity())
}
