package com.example.sol_denka_stockmanagement.database.repository.field

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
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
): IPresetRepo {

    val presetUnits = listOf(
        FieldMasterModel(
            fieldId = 1,
            fieldName = InboundInputField.WEIGHT.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),FieldMasterModel(
            fieldId = 2,
            fieldName = InboundInputField.LENGTH.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 3,
            fieldName = InboundInputField.SPECIFIC_GRAVITY.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 4,
            fieldName = InboundInputField.WIDTH.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 5,
            fieldName = InboundInputField.THICKNESS.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 6,
            fieldName = InboundInputField.LOT_NO.displayName,
            dataType = DataType.NUMBER,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 7,
            fieldName = InboundInputField.WINDER_INFO.displayName,
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 8,
            fieldName = InboundInputField.OCCURRENCE_REASON.displayName,
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 9,
            fieldName = InboundInputField.MEMO.displayName,
            dataType = DataType.TEXT,
            controlType = ControlType.INPUT
        ),
        FieldMasterModel(
            fieldId = 10,
            fieldName = InboundInputField.LOCATION.displayName,
            dataType = DataType.TEXT,
            controlType = ControlType.DROPDOWN
        ),
        FieldMasterModel(
            fieldId = 11,
            fieldName = InboundInputField.PACKING_TYPE.displayName,
            dataType = DataType.TEXT,
            controlType = ControlType.DROPDOWN
        ),
        FieldMasterModel(
            fieldId = 12,
            fieldName = InboundInputField.OCCURRED_AT.displayName,
            dataType = DataType.DATETIME,
            controlType = ControlType.DATETIMEPICKER
        ),
        FieldMasterModel(
            fieldId = 13,
            fieldName = InboundInputField.PROCESSED_AT.displayName,
            dataType = DataType.DATETIME,
            controlType = ControlType.DATETIMEPICKER
        ),
    )

    override suspend fun ensurePresetInserted() {
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