package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel

class ItemTypeFieldSettingMasterImporter(
    private val repository: ItemTypeFieldSettingMasterRepository
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

    override suspend fun persist(entities: List<ItemTypeFieldSettingMasterModel>) {
        repository.replaceAll(entities)
    }
}


