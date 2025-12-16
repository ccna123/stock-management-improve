package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel

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
                    itemTypeCode = p[1],
                    itemTypeName = p[2],
                    itemUnitId = p[3].toInt(),
                    itemCategoryId = p[4].toInt(),
                    specificGravity = p[5],
                    grade = p[6],
                    packingType = p[7],
                )
            }

        repository.insertAll(entities)
    }
}
