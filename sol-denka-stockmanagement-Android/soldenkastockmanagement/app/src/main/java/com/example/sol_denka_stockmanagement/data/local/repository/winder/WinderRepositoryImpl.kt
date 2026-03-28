package com.example.sol_denka_stockmanagement.data.local.repository.winder

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.winder.WinderDao
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel
import com.example.sol_denka_stockmanagement.domain.repository.winder.IWinderRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WinderRepositoryImpl @Inject constructor(
    private val dao: WinderDao
) : IWinderRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun getIdByName(winderName: String) = dao.getIdByName(winderName)
    override suspend fun insert(model: WinderModel) = dao.insert(model.toEntity())
    override suspend fun update(model: WinderModel) = dao.update(model.toEntity())
    override suspend fun delete(model: WinderModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<WinderModel>) = dao.upsertAll(models.map { it.toEntity() })
}