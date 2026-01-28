package com.example.sol_denka_stockmanagement.database.repository.item

import com.example.sol_denka_stockmanagement.database.dao.item.ItemCategoryDao
import com.example.sol_denka_stockmanagement.model.item.ItemCategoryModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemCategoryRepository @Inject constructor(
    private val dao: ItemCategoryDao
){
    fun get(): Flow<List<ItemCategoryModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    suspend fun getIdByName(name: String): Int = dao.getIdByName(name).toInt()

    suspend fun insert(model: ItemCategoryModel) = dao.insert(model.toEntity())
    suspend fun insertAll(model: List<ItemCategoryModel>) = dao.insertAll(model.map { it.toEntity() })
    suspend fun update(model: ItemCategoryModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemCategoryModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<ItemCategoryModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}
