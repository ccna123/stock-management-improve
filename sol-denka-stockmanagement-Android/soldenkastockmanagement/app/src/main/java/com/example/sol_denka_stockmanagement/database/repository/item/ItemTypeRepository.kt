package com.example.sol_denka_stockmanagement.database.repository.item

import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemTypeRepository @Inject constructor(
    private val dao: ItemTypeDao
) {
    fun get(): Flow<List<ItemTypeMasterEntity>> = dao.get()
    suspend fun insert(e: ItemTypeMasterEntity) = dao.insert(e)
    suspend fun update(e: ItemTypeMasterEntity) = dao.update(e)
    suspend fun delete(e: ItemTypeMasterEntity) = dao.delete(e)
}
