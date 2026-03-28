package com.example.sol_denka_stockmanagement.domain.repository.item

import com.example.sol_denka_stockmanagement.domain.model.item.ItemUnitMasterModel
import kotlinx.coroutines.flow.Flow

interface IItemUnitRepository {
    fun get(): Flow<List<ItemUnitMasterModel>>
    suspend fun countRecord(): Int
    suspend fun insert(model: ItemUnitMasterModel): Long
    suspend fun update(model: ItemUnitMasterModel)
    suspend fun delete(model: ItemUnitMasterModel)
    suspend fun upsertAll(models: List<ItemUnitMasterModel>)
}