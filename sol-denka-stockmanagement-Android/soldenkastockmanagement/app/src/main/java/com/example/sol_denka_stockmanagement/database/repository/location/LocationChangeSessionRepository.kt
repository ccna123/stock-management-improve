package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeSessionDao
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.model.location.toEntity
import com.example.sol_denka_stockmanagement.model.location.toModel
import com.example.sol_denka_stockmanagement.model.session.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeSessionRepository @Inject constructor(
    private val dao: LocationChangeSessionDao
) {
    fun get(): Flow<List<LocationChangeSessionModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getExecutedAt(): List<SessionModel> = dao.getExecutedAt()
    suspend fun insert(model: LocationChangeSessionModel) = dao.insert(model.toEntity())
    suspend fun update(model: LocationChangeSessionModel) = dao.update(model.toEntity())
    suspend fun delete(model: LocationChangeSessionModel) = dao.delete(model.toEntity())
}
