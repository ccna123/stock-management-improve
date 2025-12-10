package com.example.sol_denka_stockmanagement.database.repository.item

import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class ItemTypeRepository @Inject constructor(
    private val dao: ItemTypeDao
) {

    fun get(): Flow<List<ItemTypeMasterModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    suspend fun findByName(keyword: String) = dao.findByName(keyword)

    suspend fun insert(model: ItemTypeMasterModel) = dao.insert(model.toEntity())
    suspend fun insertAll(model: List<ItemTypeMasterModel>) = dao.insertAll(model.map { it.toEntity() })
    suspend fun update(model: ItemTypeMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemTypeMasterModel) = dao.delete(model.toEntity())
}
