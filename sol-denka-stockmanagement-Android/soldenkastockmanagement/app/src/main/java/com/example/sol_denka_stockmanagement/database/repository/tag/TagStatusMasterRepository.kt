package com.example.sol_denka_stockmanagement.database.repository.tag

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.tag.TagStatusMasterDao
import com.example.sol_denka_stockmanagement.model.csv.CsvTaskTypeModel
import com.example.sol_denka_stockmanagement.model.csv.toEntity
import com.example.sol_denka_stockmanagement.model.tag.TagStatusMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class TagStatusMasterRepository @Inject constructor(
    private val dao: TagStatusMasterDao
): IPresetRepo {

    val presetUnits = listOf(
        TagStatusMasterModel(
            tagStatusId = 1,
            statusCode = TagStatus.UNASSIGNED.statusCode,
            statusName = TagStatus.UNASSIGNED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 2,
            statusCode = TagStatus.ATTACHED.statusCode,
            statusName = TagStatus.ATTACHED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 3,
            statusCode = TagStatus.DETACHED.statusCode,
            statusName = TagStatus.DETACHED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 4,
            statusCode = TagStatus.DISABLED.statusCode,
            statusName = TagStatus.DISABLED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    )

    override suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [TagStatusMasterRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [TagStatusMasterRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<TagStatusMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }
    suspend fun insert(model: TagStatusMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: TagStatusMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagStatusMasterModel) = dao.delete(model.toEntity())
    suspend fun replaceAll(models: List<TagStatusMasterModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}