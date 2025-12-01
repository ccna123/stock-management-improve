package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel
import com.example.sol_denka_stockmanagement.model.ledger.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class ItemTypeRepository @Inject constructor(
    private val dao: ItemTypeDao
) {

    fun get(): Flow<List<ItemTypeMasterModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    suspend fun insert(model: ItemTypeMasterModel) = dao.insert(model.toEntity())
    suspend fun insertAll(model: List<ItemTypeMasterModel>) = dao.insertAll(model.map { it.toEntity() })
    suspend fun update(model: ItemTypeMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemTypeMasterModel) = dao.delete(model.toEntity())
}
