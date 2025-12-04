package com.example.sol_denka_stockmanagement.database.repository.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.model.csv.toEntity
import com.example.sol_denka_stockmanagement.model.csv.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvTaskTypeRepository @Inject constructor(
    private val dao: CsvTaskTypeDao
) {

    val presetUnits = listOf(
        CsvTaskTypeModel(
            csvTaskTypeId = 1,
            csvTaskCode = CsvTaskType.IN,
            csvTaskName = CsvTaskType.IN.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        CsvTaskTypeModel(
            csvTaskTypeId = 2,
            csvTaskCode = CsvTaskType.INVENTORY,
            csvTaskName = CsvTaskType.INVENTORY.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        CsvTaskTypeModel(
            csvTaskTypeId = 3,
            csvTaskCode = CsvTaskType.LOCATION_CHANGE,
            csvTaskName = CsvTaskType.LOCATION_CHANGE.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        CsvTaskTypeModel(
            csvTaskTypeId = 4,
            csvTaskCode = CsvTaskType.UPPER_SYSTEM,
            csvTaskName = CsvTaskType.UPPER_SYSTEM.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        CsvTaskTypeModel(
            csvTaskTypeId = 5,
            csvTaskCode = CsvTaskType.OTHER,
            csvTaskName = CsvTaskType.OTHER.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [CsvTaskTypeRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [CsvTaskTypeRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<CsvTaskTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: CsvTaskTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: CsvTaskTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: CsvTaskTypeModel) = dao.delete(model.toEntity())
}