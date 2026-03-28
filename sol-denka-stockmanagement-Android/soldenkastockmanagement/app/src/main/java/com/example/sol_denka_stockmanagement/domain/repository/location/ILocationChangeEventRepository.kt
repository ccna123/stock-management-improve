package com.example.sol_denka_stockmanagement.domain.repository.location

import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventForExportModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventModel
import kotlinx.coroutines.flow.Flow

interface ILocationChangeEventRepository {
    fun get(): Flow<List<LocationChangeEventModel>>
    suspend fun getEventBySessionId(sessionId: Int): List<LocationChangeEventForExportModel>
    suspend fun insert(model: LocationChangeEventModel): Long
    suspend fun update(model: LocationChangeEventModel)
    suspend fun delete(model: LocationChangeEventModel)
}