package com.example.sol_denka_stockmanagement.database.repository.winder

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.WinderType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.winder.WinderInfoDao
import com.example.sol_denka_stockmanagement.model.winder.WinderInfoModel
import com.example.sol_denka_stockmanagement.model.winder.toEntity
import com.example.sol_denka_stockmanagement.model.winder.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WinderInfoRepository @Inject constructor(
    private val dao: WinderInfoDao
): IPresetRepo {

    private val presetUnits = listOf(
        WinderInfoModel(
            winderId = 1,
            winderName = WinderType.MACHINE_2.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 2,
            winderName = WinderType.SLITTING_B_F.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 3,
            winderName = WinderType.SLITTING_B_B.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 4,
            winderName = WinderType.MACHINE_3_LINE_1.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 5,
            winderName = WinderType.MACHINE_3_LINE_2.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 6,
            winderName = WinderType.MACHINE_4_LINE_1.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 7,
            winderName = WinderType.MACHINE_4_LINE_2.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        WinderInfoModel(
            winderId = 8,
            winderName = WinderType.OTHERS.displayName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
    )

    override suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [WinderInfoRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [WinderInfoRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<WinderInfoModel>> =
        dao.get().map { entityList -> entityList.map { it.toModel() } }

    suspend fun insert(model: WinderInfoModel) = dao.insert(model.toEntity())
    suspend fun update(model: WinderInfoModel) = dao.update(model.toEntity())
    suspend fun delete(model: WinderInfoModel) = dao.delete(model.toEntity())
}