package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemTypeRepository @Inject constructor(
    private val dao: ItemTypeDao
) {

    private val presetTypes = listOf(
        ItemTypeMasterModel(
            101, "RM001", "原材料ゴム1", 3, generateTimeStamp(),
            generateTimeStamp()
        ),
        ItemTypeMasterModel(
            102, "RM002", "原材料ゴム2", 3, generateTimeStamp(),
            generateTimeStamp()
        ),
        ItemTypeMasterModel(
            103, "FM001", "フィルムシート1", 7,
            generateTimeStamp(), generateTimeStamp()
        ),
        ItemTypeMasterModel(
            104, "FM002", "フィルムシート2", 7,
            generateTimeStamp(), generateTimeStamp()
        ),
        ItemTypeMasterModel(105, "CT001", "カット品A", 6, generateTimeStamp(), generateTimeStamp()),
        ItemTypeMasterModel(106, "CT002", "カット品B", 6, generateTimeStamp(), generateTimeStamp()),
        ItemTypeMasterModel(
            107, "PD001", "製品タイプA", 2, generateTimeStamp(),
            generateTimeStamp()
        ),
        ItemTypeMasterModel(
            108, "PD002", "製品タイプB", 2, generateTimeStamp(),
            generateTimeStamp()
        ),
        ItemTypeMasterModel(
            109, "QC001", "品質検査サンプル1", 1,
            generateTimeStamp(), generateTimeStamp()
        ),
        ItemTypeMasterModel(
            110, "QC002", "品質検査サンプル2", 1,
            generateTimeStamp(), generateTimeStamp()
        )
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            ensurePresetInserted()
        }
    }

    private suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()

        if (existing.isEmpty()) {
            presetTypes.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "[ItemTypeRepository] Preset ItemTypes inserted")
        } else {
            Log.i("TSS", "[ItemTypeRepository] Preset already exists → skip")
        }
    }

    fun get(): Flow<List<ItemTypeMasterModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    suspend fun insert(model: ItemTypeMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: ItemTypeMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemTypeMasterModel) = dao.delete(model.toEntity())
}
