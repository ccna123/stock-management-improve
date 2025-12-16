package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
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
): IPresetRepo {

    private val presetUnits = listOf(
        ItemUnitMasterModel(
            itemUnitId = 1,
            itemUnitCode = ItemUnit.KG.engName,
            unitCategory = 1,
            itemUnitName = ItemUnit.KG.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ItemUnitMasterModel(
            itemUnitId = 2,
            itemUnitCode = ItemUnit.TON.engName,
            unitCategory = 1,
            itemUnitName = ItemUnit.TON.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ItemUnitMasterModel(
            itemUnitId = 3,
            itemUnitCode = ItemUnit.HON.engName,
            unitCategory = 2,
            itemUnitName = ItemUnit.HON.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ItemUnitMasterModel(
            itemUnitId = 4,
            itemUnitCode = ItemUnit.MAI.engName,
            unitCategory = 2,
            itemUnitName = ItemUnit.MAI.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ItemUnitMasterModel(
            itemUnitId = 5,
            itemUnitCode = ItemUnit.KAN.engName,
            unitCategory = 2,
            itemUnitName = ItemUnit.KAN.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ItemUnitMasterModel(
            itemUnitId = 6,
            itemUnitCode = ItemUnit.TAI.engName,
            unitCategory = 2,
            itemUnitName = ItemUnit.TAI.jpName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    override suspend fun ensurePresetInserted() {
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