package com.example.sol_denka_stockmanagement.database.repository.outbound

import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundSessionDao
import com.example.sol_denka_stockmanagement.model.outbound.OutboundSessionModel
import com.example.sol_denka_stockmanagement.model.outbound.toEntity
import com.example.sol_denka_stockmanagement.model.outbound.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutboundSessionRepository @Inject constructor(
    private val dao: OutboundSessionDao
) {
    fun get(): Flow<List<OutboundSessionModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: OutboundSessionModel) = dao.insert(model.toEntity())
    suspend fun update(model: OutboundSessionModel) = dao.update(model.toEntity())
    suspend fun delete(model: OutboundSessionModel) = dao.delete(model.toEntity())
}
