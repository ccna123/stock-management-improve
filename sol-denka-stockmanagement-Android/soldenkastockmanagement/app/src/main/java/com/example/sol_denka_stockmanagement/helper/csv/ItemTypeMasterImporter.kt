package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.util.toNullIfBlank

class ItemTypeMasterImporter(
    private val repository: ItemTypeRepository
): ICsvImport {

    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",") }
            .map { p ->
                ItemTypeMasterModel(
                    itemTypeId = p[0].toInt(),
                    itemTypeCode = p[1].toNullIfBlank(),
                    itemTypeName = p[2],
                    itemCountUnitId = p[3].toIntOrNull(),
                    itemWeightUnitId = p[4].toIntOrNull(),
                    itemCategoryId = p[5].toInt(),
                    specificGravity = p[6].toNullIfBlank(),
                    grade = p[7].toNullIfBlank(),
                    packingType = p[8].toNullIfBlank(),
                )
            }

        repository.replaceAll(entities)
    }
}
