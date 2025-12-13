package com.example.sol_denka_stockmanagement.database.repository.item

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.Category
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.item.ItemCategoryDao
import com.example.sol_denka_stockmanagement.model.item.ItemCategoryModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemCategoryRepository @Inject constructor(
    private val dao: ItemCategoryDao
): IPresetRepo {


    private val presetUnits = listOf(
        ItemCategoryModel(
            itemCategoryId = 1,
            itemCategoryName = Category.SUB_MATERIAL.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        ),
        ItemCategoryModel(
            itemCategoryId = 2,
            itemCategoryName = Category.SUB_RAW_MATERIAL.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        ),
        ItemCategoryModel(
            itemCategoryId = 3,
            itemCategoryName = Category.INTERMEDIATE_PRODUCT.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        ),
        ItemCategoryModel(
            itemCategoryId = 4,
            itemCategoryName = Category.SEMI_FINISHED_PRODUCT.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        ),
        ItemCategoryModel(
            itemCategoryId = 5,
            itemCategoryName = Category.NON_STANDARD_ITEM.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        ),
    )

    override suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [ItemCategoryRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [ItemCategoryRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<ItemCategoryModel>> =
        dao.get().map { list -> list.map { it.toModel() } }

    suspend fun getIdByName(name: String): Int = dao.getIdByName(name).toInt()

    suspend fun insert(model: ItemCategoryModel) = dao.insert(model.toEntity())
    suspend fun insertAll(model: List<ItemCategoryModel>) = dao.insertAll(model.map { it.toEntity() })
    suspend fun update(model: ItemCategoryModel) = dao.update(model.toEntity())
    suspend fun delete(model: ItemCategoryModel) = dao.delete(model.toEntity())
}
