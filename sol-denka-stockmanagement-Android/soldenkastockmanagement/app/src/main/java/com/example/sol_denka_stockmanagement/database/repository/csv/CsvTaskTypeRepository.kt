package com.example.sol_denka_stockmanagement.database.repository.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.model.csv.toEntity
import com.example.sol_denka_stockmanagement.model.csv.toModel
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class CsvTaskTypeRepository @Inject constructor(
    private val dao: CsvTaskTypeDao
): IPresetRepo {

    override suspend fun ensurePresetInserted() {
    }

    fun get(): Flow<List<CsvTaskTypeModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun getIdByTaskCode(taskCode: String) = dao.getIdByTaskCode(taskCode)
    suspend fun insert(model: CsvTaskTypeModel) = dao.insert(model.toEntity())
    suspend fun update(model: CsvTaskTypeModel) = dao.update(model.toEntity())
    suspend fun delete(model: CsvTaskTypeModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<CsvTaskTypeModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}