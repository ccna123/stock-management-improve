package com.example.sol_denka_stockmanagement.helper.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel

class ItemTypeMasterImporter(
    private val repository: ItemTypeRepository
): ICsvImport {

    private fun parseNullableInt(v: String?): Int? {
        if (v == null) return null
        if (v.isBlank()) return null
        return v.toFloatOrNull()?.toInt()
    }

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
                    createdAt = generateTimeStamp(),
                    updatedAt = generateTimeStamp(),
                )
            }

        repository.insertAll(entities)
    }
}
