package com.example.sol_denka_stockmanagement.data.local.repository.location

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationMasterRepositoryImpl @Inject constructor(
    private val dao: LocationDao
) : ILocationMasterRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun insert(model: LocationMasterModel) = dao.insert(model.toEntity())
    override suspend fun update(model: LocationMasterModel) = dao.update(model.toEntity())
    override suspend fun delete(model: LocationMasterModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<LocationMasterModel>) = dao.upsertAll(models.map { it.toEntity() })
}