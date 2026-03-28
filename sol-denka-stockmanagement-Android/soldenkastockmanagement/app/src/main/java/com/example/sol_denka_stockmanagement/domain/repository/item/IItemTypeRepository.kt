package com.example.sol_denka_stockmanagement.domain.repository.item

import com.example.sol_denka_stockmanagement.domain.model.item.ItemTypeMasterModel
import kotlinx.coroutines.flow.Flow

interface IItemTypeRepository {
    fun get(): Flow<List<ItemTypeMasterModel>>
    suspend fun countRecord(): Int
    suspend fun getItemTypeByCategoryId(categoryId: Int): List<ItemTypeMasterModel>
    suspend fun getItemTypeIdByItemName(itemName: String): Int
    suspend fun insert(model: ItemTypeMasterModel)
    suspend fun update(model: ItemTypeMasterModel)
    suspend fun delete(model: ItemTypeMasterModel)
    suspend fun upsertAll(models: List<ItemTypeMasterModel>)
}
