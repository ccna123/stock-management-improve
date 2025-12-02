package com.example.sol_denka_stockmanagement.helper.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel

class LedgerItemMasterImporter(
    private val repository: LedgerItemRepository
): ICsvImport {

    private fun parseNullableInt(v: String?): Int? {
        if (v == null) return null
        if (v.isBlank()) return null
        return v.toFloatOrNull()?.toInt()
    }

    private fun parseBoolean(v: String?): Boolean {
        if (v == null) return false
        return v == "1" || v.equals("true", ignoreCase = true)
    }

    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",") }
            .map { p ->
                LedgerItemModel(
                    ledgerItemId = p[0].toInt(),
                    itemTypeId = p[1].toInt(),
                    locationId = p[2].toInt(),
                    isInStock = parseBoolean(p[3]),

                    weight = parseNullableInt(p[4]),
                    grade = p[5].ifBlank { null },
                    specificGravity = parseNullableInt(p[6]),
                    thickness = parseNullableInt(p[7]),
                    width = parseNullableInt(p[8]),
                    length = parseNullableInt(p[9]),
                    quantity = parseNullableInt(p[10]),

                    winderInfo = p[11].ifBlank { null },
                    misrollReason = p[12].ifBlank { null },

                    createdAt = generateTimeStamp(),
                    updatedAt = generateTimeStamp()
                )
            }

        repository.insertAll(entities)
    }
}
