package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val dao: LocationDao
) {
    fun get(): Flow<List<LocationMasterEntity>> = dao.get()
    suspend fun insert(e: LocationMasterEntity) = dao.insert(e)
    suspend fun update(e: LocationMasterEntity) = dao.update(e)
    suspend fun delete(e: LocationMasterEntity) = dao.delete(e)
}
