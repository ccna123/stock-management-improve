package com.example.sol_denka_stockmanagement.database.repository.inventory

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultTypeModel
import com.example.sol_denka_stockmanagement.model.inventory.toEntity
import com.example.sol_denka_stockmanagement.model.inventory.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryResultTypeRepository @Inject constructor(
    private val dao: InventoryResultTypeDao
) {

    val presetUnits = listOf(
        InventoryResultTypeModel(
            inventoryResultTypeId = 1,
            inventoryResultTypeCode = InventoryResultType.FOUND_OK,
            inventoryResultTypeName = InventoryResultType.FOUND_OK.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        InventoryResultTypeModel(
            inventoryResultTypeId = 2,
            inventoryResultTypeCode = InventoryResultType.FOUND_WRONG_LOCATION,
            inventoryResultTypeName = InventoryResultType.FOUND_WRONG_LOCATION.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        InventoryResultTypeModel(
            inventoryResultTypeId = 3,
            inventoryResultTypeCode = InventoryResultType.NOT_FOUND,
            inventoryResultTypeName = InventoryResultType.NOT_FOUND.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        InventoryResultTypeModel(
            inventoryResultTypeId = 4,
            inventoryResultTypeCode = InventoryResultType.FOUND_OVER_STOCK,
            inventoryResultTypeName = InventoryResultType.FOUND_OVER_STOCK.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [InventoryResultTypeRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [InventoryResultTypeRepository] Preset already exists → skip insert")
        }
    }


    fun get(): Flow<List<InventoryResultTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: InventoryResultTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: InventoryResultTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: InventoryResultTypeModel) = dao.delete(model.toEntity())
}