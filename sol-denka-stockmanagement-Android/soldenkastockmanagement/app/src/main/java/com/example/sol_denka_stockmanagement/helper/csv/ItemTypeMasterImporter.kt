package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel

class ItemTypeMasterImporter(
    private val repository: ItemTypeRepository
) : CsvImporter<ItemTypeMasterModel>() {

    override val requiredHeaders = setOf(
        "item_type_id",
        "item_type_code",
        "item_type_name",
        "item_count_unit_id",
        "item_weight_unit_id",
        "item_category_id",
        "specific_gravity",
        "grade",
        "packing_type"
    )

    override fun mapRow(row: CsvRow): ItemTypeMasterModel {
        return ItemTypeMasterModel(
            itemTypeId = row.int("item_type_id")!!,
            itemTypeCode = row.string("item_type_code")!!,
            itemTypeName = row.string("item_type_name")!!,
            itemCountUnitId = row.int("item_count_unit_id")!!,
            itemWeightUnitId = row.int("item_weight_unit_id")!!,
            itemCategoryId = row.int("item_category_id")!!,
            specificGravity = row.string("specific_gravity"),
            grade = row.string("grade"),
            packingType = row.string("packing_type")
        )
    }

    override suspend fun persist(entities: List<ItemTypeMasterModel>) {
        repository.replaceAll(entities)
    }
}
