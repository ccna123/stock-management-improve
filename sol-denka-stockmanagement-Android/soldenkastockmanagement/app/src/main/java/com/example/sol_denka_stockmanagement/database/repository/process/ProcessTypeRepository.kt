package com.example.sol_denka_stockmanagement.database.repository.process

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
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
import kotlin.collections.map

@Singleton
class ProcessTypeRepository @Inject constructor(
    private val dao: ProcessTypeDao
): IPresetRepo {

    val presetUnits = listOf(
        ProcessTypeModel(
            processTypeId = 1,
            processCode = ProcessMethod.USE,
            processName = ProcessMethod.USE.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processTypeId = 2,
            processCode = ProcessMethod.SALE,
            processName = ProcessMethod.SALE.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processTypeId = 3,
            processCode = ProcessMethod.CRUSH,
            processName = ProcessMethod.CRUSH.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processTypeId = 4,
            processCode = ProcessMethod.DISCARD,
            processName = ProcessMethod.DISCARD.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        ProcessTypeModel(
            processTypeId = 5,
            processCode = ProcessMethod.PROCESS,
            processName = ProcessMethod.PROCESS.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    override suspend fun ensurePresetInserted() {
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
    suspend fun replaceAll(models: List<ProcessTypeModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}