package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
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
                    winderInfoId = p[3].toInt(),
                    isInStock = parseBoolean(p[4]),
                    weight = parseNullableInt(p[5]),
                    width = parseNullableInt(p[6]),
                    length = parseNullableInt(p[7]),
                    thickness = parseNullableInt(p[8]),
                    lotNo = parseNullableInt(p[9]),
                    occurrenceReason = p[10].ifBlank { null },
                    quantity = parseNullableInt(p[11]),
                    memo = p[12],
                    occurredAt = p[13],
                    processedAt = p[14],
                )
            }

        repository.insertAll(entities)
    }
}
