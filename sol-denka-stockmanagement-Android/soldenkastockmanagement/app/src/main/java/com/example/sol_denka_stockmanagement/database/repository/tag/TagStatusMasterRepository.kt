package com.example.sol_denka_stockmanagement.database.repository.tag

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.tag.TagStatusMasterDao
import com.example.sol_denka_stockmanagement.model.tag.TagStatusMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach
import kotlin.collections.map

@Singleton
class TagStatusMasterRepository @Inject constructor(
    private val dao: TagStatusMasterDao
): IPresetRepo {

    val presetUnits = listOf(
        TagStatusMasterModel(
            tagStatusId = 1,
            statusCode = TagScanStatus.UNASSIGNED.statusCode,
            statusName = TagScanStatus.UNASSIGNED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 2,
            statusCode = TagScanStatus.ATTACHED.statusCode,
            statusName = TagScanStatus.ATTACHED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 3,
            statusCode = TagScanStatus.DETACHED.statusCode,
            statusName = TagScanStatus.DETACHED.statusName,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        ),
        TagStatusMasterModel(
            tagStatusId = 4,
            statusCode = TagScanStatus.DISABLED.statusCode,
            statusName = TagScanStatus.DISABLED.statusName,
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
}