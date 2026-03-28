package com.example.sol_denka_stockmanagement.domain.repository.location

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import kotlinx.coroutines.flow.Flow

interface ILocationMasterRepository {
    fun get(): Flow<List<LocationMasterModel>>
    suspend fun countRecord(): Int
    suspend fun insert(model: LocationMasterModel): Long
    suspend fun update(model: LocationMasterModel)
    suspend fun delete(model: LocationMasterModel)
    suspend fun upsertAll(models: List<LocationMasterModel>)
}