package com.example.sol_denka_stockmanagement.database.repository.location

import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.location.toEntity
import com.example.sol_denka_stockmanagement.model.location.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationMasterRepository @Inject constructor(
    private val dao: LocationDao
) {
    fun get(): Flow<List<LocationMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun getLocationIdByName(locationName: String) = dao.getLocationIdByName(locationName)
    suspend fun insert(model: LocationMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: LocationMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: LocationMasterModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<LocationMasterModel>) =
        dao.replaceAll(models.map { it.toEntity() })
}
