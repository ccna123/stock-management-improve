package com.example.sol_denka_stockmanagement.domain.repository.item

import com.example.sol_denka_stockmanagement.domain.model.item.ItemCategoryModel
import kotlinx.coroutines.flow.Flow

interface IItemCategoryRepository {
    fun get(): Flow<List<ItemCategoryModel>>
    suspend fun countRecord(): Int
    suspend fun getIdByName(name: String): Int
    suspend fun insert(model: ItemCategoryModel)
    suspend fun insertAll(models: List<ItemCategoryModel>)
    suspend fun update(model: ItemCategoryModel)
    suspend fun delete(model: ItemCategoryModel)
    suspend fun upsertAll(models: List<ItemCategoryModel>)
}