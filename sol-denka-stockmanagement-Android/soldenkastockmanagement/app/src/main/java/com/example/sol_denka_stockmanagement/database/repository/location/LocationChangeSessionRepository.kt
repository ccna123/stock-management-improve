package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeSessionDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeSessionRepository @Inject constructor(
    private val dao: LocationChangeSessionDao
) {
    fun get(): Flow<List<LocationChangeSessionEntity>> = dao.get()
    suspend fun insert(e: LocationChangeSessionEntity) = dao.insert(e)
    suspend fun update(e: LocationChangeSessionEntity) = dao.update(e)
    suspend fun delete(e: LocationChangeSessionEntity) = dao.delete(e)
}
