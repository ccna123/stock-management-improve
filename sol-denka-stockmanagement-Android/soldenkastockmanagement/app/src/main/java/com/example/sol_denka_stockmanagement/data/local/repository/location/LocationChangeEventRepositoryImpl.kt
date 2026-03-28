package com.example.sol_denka_stockmanagement.data.local.repository.location

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeEventDao
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeEventRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeEventRepositoryImpl @Inject constructor(
    private val dao: LocationChangeEventDao
) : ILocationChangeEventRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getEventBySessionId(sessionId: Int) = dao.getEventBySessionId(sessionId)
    override suspend fun insert(model: LocationChangeEventModel) = dao.insert(model.toEntity())
    override suspend fun update(model: LocationChangeEventModel) = dao.update(model.toEntity())
    override suspend fun delete(model: LocationChangeEventModel) = dao.delete(model.toEntity())
}