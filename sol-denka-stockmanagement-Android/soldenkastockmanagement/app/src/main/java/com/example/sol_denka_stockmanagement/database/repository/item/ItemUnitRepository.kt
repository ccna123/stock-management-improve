package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.model.item.ItemUnitMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemUnitRepository @Inject constructor(
    private val dao: ItemUnitDao
) {

    private val presetUnits = listOf(
        ItemUnitMasterModel(1, "UNIT-PCS", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(2, "UNIT-BOX", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(3, "UNIT-ROLL", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(4, "UNIT-PALLET", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(5, "UNIT-BAG", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(6, "UNIT-SET", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(7, "UNIT-KG", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(8, "UNIT-M", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(9, "UNIT-CM", generateTimeStamp(), generateTimeStamp()),
        ItemUnitMasterModel(10, "UNIT-L", generateTimeStamp(), generateTimeStamp())
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
