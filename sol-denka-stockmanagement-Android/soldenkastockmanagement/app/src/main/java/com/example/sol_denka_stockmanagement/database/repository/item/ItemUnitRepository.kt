package com.example.sol_denka_stockmanagement.database.repository.item

import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemUnitRepository @Inject constructor(
    private val dao: ItemUnitDao
) {
    fun get(): Flow<List<ItemUnitMasterEntity>> = dao.get()
    suspend fun insert(e: ItemUnitMasterEntity) = dao.insert(e)
    suspend fun update(e: ItemUnitMasterEntity) = dao.update(e)
    suspend fun delete(e: ItemUnitMasterEntity) = dao.delete(e)
}
