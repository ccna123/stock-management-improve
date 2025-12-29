package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.model.field.ItemTypeFieldSettingMasterModel

class ItemTypeFieldSettingMasterImporter(
    private val repository: ItemTypeFieldSettingMasterRepository
) : CsvImporter<ItemTypeFieldSettingMasterModel>() {

    override fun parse(lines: List<String>): List<ItemTypeFieldSettingMasterModel> {
        return lines
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
    }

    override suspend fun persist(entities: List<ItemTypeFieldSettingMasterModel>) {
        repository.replaceAll(buffer)
    }
}

