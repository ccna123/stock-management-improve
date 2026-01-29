package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import java.math.BigDecimal

class ItemTypeMasterImporter(
    private val repository: ItemTypeRepository,
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
        "packing_type",
        "memo",
        "unit_weight"
    )

    override fun mapRow(row: CsvRow): ItemTypeMasterModel {
        return ItemTypeMasterModel(
            itemTypeId = row.int("item_type_id")!!,
            itemTypeCode = row.stringWithLength("item_type_code", min = 1, max = 50),
            itemTypeName = row.stringWithLength("item_type_name", min = 1, max = 100)!!,
            itemCountUnitId = row.int("item_count_unit_id"),
            itemWeightUnitId = row.int("item_weight_unit_id"),
            itemCategoryId = row.int("item_category_id")!!,
            specificGravity = row.decimal3("specific_gravity", min = BigDecimal("0.000"), max = BigDecimal("99.999")),
            grade = row.stringWithLength("grade", min = 1, max = 50),
            packingType = row.stringWithLength("packing_type", min = 1, max = 50),
            memo = row.stringWithLength("memo", min = 1, max = 500),
            unitWeight = row.long("unit_weight"),
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<ItemTypeMasterModel>) {
        repository.upsertAll(entities)
    }
}
