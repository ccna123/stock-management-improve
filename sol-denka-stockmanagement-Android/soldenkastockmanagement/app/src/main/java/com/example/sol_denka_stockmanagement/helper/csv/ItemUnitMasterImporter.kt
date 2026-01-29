package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.item.ItemUnitRepository
import com.example.sol_denka_stockmanagement.model.item.ItemUnitMasterModel

class ItemUnitMasterImporter(
    private val repository: ItemUnitRepository,
): CsvImporter<ItemUnitMasterModel>() {

    override val requiredHeaders = setOf(
        "item_unit_id",
        "unit_category",
        "item_unit_code",
        "item_unit_name"
    )

    override fun mapRow(row: CsvRow): ItemUnitMasterModel {
        return ItemUnitMasterModel(
            itemUnitId = row.int("item_unit_id")!!,
            unitCategory = row.int("unit_category")!!,
            itemUnitCode = row.string("item_unit_code")!!,
            itemUnitName = row.string("item_unit_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<ItemUnitMasterModel>) {
        repository.upsertAll(entities)
    }
}