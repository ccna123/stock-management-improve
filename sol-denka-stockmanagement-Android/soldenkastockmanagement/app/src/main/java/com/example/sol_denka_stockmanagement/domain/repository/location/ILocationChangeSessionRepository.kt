package com.example.sol_denka_stockmanagement.domain.repository.location

import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

interface ILocationChangeSessionRepository {
    fun get(): Flow<List<LocationChangeSessionModel>>
    suspend fun getSession(): List<SessionModel>
    suspend fun insert(model: LocationChangeSessionModel): Long
    suspend fun update(model: LocationChangeSessionModel)
    suspend fun delete(model: LocationChangeSessionModel)
}