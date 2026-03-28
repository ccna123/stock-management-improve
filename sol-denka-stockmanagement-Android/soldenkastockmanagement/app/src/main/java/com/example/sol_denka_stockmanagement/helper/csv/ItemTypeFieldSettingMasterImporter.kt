package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel

class ItemTypeFieldSettingMasterImporter(
    private val repository: com.example.sol_denka_stockmanagement.domain.repository.field.IItemTypeFieldSettingMasterRepository,
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
    override suspend fun replaceAllWithNewData(entities: List<ItemTypeFieldSettingMasterModel>) {
        repository.upsertAll(entities)
    }
}


