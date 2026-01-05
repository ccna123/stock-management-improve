package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel

class ItemTypeFieldSettingMasterImporter(
    private val repository: ItemTypeFieldSettingMasterRepository,
    private val db: AppDatabase
) : CsvImporter<ItemTypeFieldSettingMasterModel>() {

    override val requiredHeaders = setOf(
        "item_type_id",
        "field_id",
        "is_required",
        "is_visible"
    )

    override fun mapRow(row: CsvRow): ItemTypeFieldSettingMasterModel {
        return ItemTypeFieldSettingMasterModel(
            itemTypeId = row.int("item_type_id")!!,
            fieldId = row.int("field_id")!!,
            isRequired = row.boolean("is_required"),
            isVisible = row.boolean("is_visible")
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<ItemTypeFieldSettingMasterModel>) {
        repository.replaceAll(entities)
    }
}


