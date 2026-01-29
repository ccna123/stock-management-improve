package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.field.FieldMasterRepository
import com.example.sol_denka_stockmanagement.model.field.FieldMasterModel

class FieldMasterImporter(
    private val repository: FieldMasterRepository,
): CsvImporter<FieldMasterModel>() {

    override val requiredHeaders = setOf(
        "field_id",
        "field_name",
        "field_code",
        "data_type",
        "control_type",
    )

    override fun mapRow(row: CsvRow): FieldMasterModel {
        return FieldMasterModel(
            fieldId = row.int("field_id")!!,
            fieldName = row.string("field_name")!!,
            fieldCode = row.string("field_code")!!,
            dataType = row.string("data_type")!!,
            controlType = row.string("control_type")!!,
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<FieldMasterModel>) {
        repository.upsertAll(entities)
    }
}