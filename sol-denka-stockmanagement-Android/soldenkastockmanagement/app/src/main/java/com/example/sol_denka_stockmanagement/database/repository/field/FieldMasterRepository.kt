package com.example.sol_denka_stockmanagement.database.repository.field

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.model.field.FieldMasterModel
import com.example.sol_denka_stockmanagement.model.field.toEntity
import com.example.sol_denka_stockmanagement.model.field.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldMasterRepository @Inject constructor(
    private val dao: FieldMasterDao
) {

    val presetUnits = listOf(
        FieldMasterModel(
            fieldId = 1,
            fieldName = "重量",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),FieldMasterModel(
            fieldId = 2,
            fieldName = "長さ",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 3,
            fieldName = "比重",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 4,
            fieldName = "巾",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 5,
            fieldName = "厚み",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 6,
            fieldName = "Lot No",
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 7,
            fieldName = "巻き取り機情報",
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 8,
            fieldName = "ミスロールになった理由",
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 9,
            fieldName = "備考",
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 10,
            fieldName = "保管場所",
            dataType = DataType.TEXT,
            controlType = ControlType.DROPDOWN
        ),
    )

    suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            presetUnits.forEach { dao.insert(it.toEntity()) }
            Log.i("TSS", "📦 [FieldMasterRepository] Preset Item Units inserted into DB")
        } else {
            Log.i("TSS", "📦 [FieldMasterRepository] Preset already exists → skip insert")
        }
    }

    fun get(): Flow<List<FieldMasterModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }
    suspend fun insert(model: FieldMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: FieldMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: FieldMasterModel) = dao.delete(model.toEntity())
}