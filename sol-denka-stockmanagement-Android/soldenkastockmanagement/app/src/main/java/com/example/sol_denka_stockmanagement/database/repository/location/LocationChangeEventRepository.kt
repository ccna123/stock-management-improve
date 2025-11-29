package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeEventDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeEventRepository @Inject constructor(
    private val dao: LocationChangeEventDao
) {
    fun get(): Flow<List<LocationChangeEventEntity>> = dao.get()
    suspend fun insert(e: LocationChangeEventEntity) = dao.insert(e)
    suspend fun update(e: LocationChangeEventEntity) = dao.update(e)
    suspend fun delete(e: LocationChangeEventEntity) = dao.delete(e)
}
