package com.example.sol_denka_stockmanagement.data.local.repository.location

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeSessionDao
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeSessionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeSessionRepositoryImpl @Inject constructor(
    private val dao: LocationChangeSessionDao
) : ILocationChangeSessionRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun getSession() = dao.getSession()
    override suspend fun insert(model: LocationChangeSessionModel) = dao.insert(model.toEntity())
    override suspend fun update(model: LocationChangeSessionModel) = dao.update(model.toEntity())
    override suspend fun delete(model: LocationChangeSessionModel) = dao.delete(model.toEntity())
}