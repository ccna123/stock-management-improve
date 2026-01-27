package com.example.sol_denka_stockmanagement.database.repository.winder

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.WinderType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.winder.WinderDao
import com.example.sol_denka_stockmanagement.model.winder.WinderModel
import com.example.sol_denka_stockmanagement.model.winder.toEntity
import com.example.sol_denka_stockmanagement.model.winder.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class WinderRepository @Inject constructor(
    private val dao: WinderDao
): IPresetRepo {


    override suspend fun ensurePresetInserted() {
    }

    fun get(): Flow<List<WinderModel>> =
        dao.get().map { entityList -> entityList.map { it.toModel() } }

    suspend fun getIdByName(winderName: String) = dao.getIdByName(winderName)

    suspend fun insert(model: WinderModel) = dao.insert(model.toEntity())
    suspend fun update(model: WinderModel) = dao.update(model.toEntity())
    suspend fun delete(model: WinderModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<WinderModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}