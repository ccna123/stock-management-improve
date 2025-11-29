package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeEventDao
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.toEntity
import com.example.sol_denka_stockmanagement.model.location.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeEventRepository @Inject constructor(
    private val dao: LocationChangeEventDao
) {
    fun get(): Flow<List<LocationChangeEventModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: LocationChangeEventModel) = dao.insert(model.toEntity())
    suspend fun update(model: LocationChangeEventModel) = dao.update(model.toEntity())
    suspend fun delete(model: LocationChangeEventModel) = dao.delete(model.toEntity())
}
