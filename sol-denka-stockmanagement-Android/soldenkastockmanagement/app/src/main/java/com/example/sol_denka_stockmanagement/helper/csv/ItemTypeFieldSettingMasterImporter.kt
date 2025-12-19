package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel

class ItemTypeFieldSettingMasterImporter(
    private val repository: ItemTypeFieldSettingMasterRepository
): ICsvImport {
    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",") }
            .map { p ->
                ItemTypeFieldSettingMasterModel(
                    itemTypeId = p[0].toInt(),
                    fieldId = p[1].toInt(),
                    isRequired = p[2] == "1",
                    isVisible = p[3] == "1"
                )
            }

        repository.replaceAll(entities)
    }
}
