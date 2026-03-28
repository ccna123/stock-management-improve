package com.example.sol_denka_stockmanagement.data.local.repository.item

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.domain.model.item.ItemUnitMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemUnitRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemUnitRepositoryImpl @Inject constructor(
    private val dao: ItemUnitDao
) : IItemUnitRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override suspend fun insert(model: ItemUnitMasterModel) = dao.insert(model.toEntity())
    override suspend fun update(model: ItemUnitMasterModel) = dao.update(model.toEntity())
    override suspend fun delete(model: ItemUnitMasterModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<ItemUnitMasterModel>) = dao.upsertAll(models.map { it.toEntity() })
}