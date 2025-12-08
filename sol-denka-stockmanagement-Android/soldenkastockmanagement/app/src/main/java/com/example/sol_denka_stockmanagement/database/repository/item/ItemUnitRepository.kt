package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.ItemUnit
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.model.item.ItemUnitMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemUnitRepository @Inject constructor(
    private val dao: ItemUnitDao
) {

    private val presetUnits = listOf(
        ItemUnitMasterModel(itemUnitCode = ItemUnit.KG.displayName, createdAt = generateTimeStamp(), updatedAt = generateTimeStamp()),
        ItemUnitMasterModel(itemUnitCode = ItemUnit.TON.displayName, createdAt = generateTimeStamp(), updatedAt = generateTimeStamp()),
        ItemUnitMasterModel(itemUnitCode = ItemUnit.HON.displayName, createdAt = generateTimeStamp(), updatedAt = generateTimeStamp()),
        ItemUnitMasterModel(itemUnitCode =  ItemUnit.MAI.displayName, createdAt = generateTimeStamp(), updatedAt = generateTimeStamp()),
    )
    suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [ItemUnitRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [ItemUnitRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<ItemUnitMasterModel>> =
        dao.get().map { entityList -> entityList.map { it.toModel() } }

    suspend fun insert(model: ItemUnitMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: ItemUnitMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemUnitMasterModel) = dao.delete(model.toEntity())
}