package com.example.sol_denka_stockmanagement.database.repository.process

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.model.process.toEntity
import com.example.sol_denka_stockmanagement.model.process.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessTypeRepository @Inject constructor(
    private val dao: ProcessTypeDao
) {

    val presetUnits = listOf(
        ProcessTypeModel(
            processCode = ProcessMethod.USE,
            processName = ProcessMethod.USE.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processCode = ProcessMethod.SALE,
            processName = ProcessMethod.SALE.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processCode = ProcessMethod.CRUSH,
            processName = ProcessMethod.CRUSH.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [ProcessTypeRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [ProcessTypeRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<ProcessTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun getIdByName(processTypeName: String) = dao.getIdByName(processTypeName)

    suspend fun insert(model: ProcessTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: ProcessTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: ProcessTypeModel) = dao.delete(model.toEntity())
}