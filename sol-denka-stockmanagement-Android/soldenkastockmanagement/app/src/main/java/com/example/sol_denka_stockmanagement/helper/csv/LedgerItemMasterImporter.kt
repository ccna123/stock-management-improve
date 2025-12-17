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
                    tagId = p[3].toInt(),
                    winderId = p[4].toInt(),
                    isInStock = parseBoolean(p[5]),
                    weight = parseNullableInt(p[6]),
                    width = parseNullableInt(p[7]),
                    length = parseNullableInt(p[8]),
                    thickness = parseNullableInt(p[9]),
                    lotNo = p[10],
                    occurrenceReason = p[11].ifBlank { null },
                    quantity = parseNullableInt(p[12]),
                    memo = p[13],
                    occurredAt = p[14],
                    processedAt = p[15],
                    registeredAt = p[16],
                    updatedAt = p[17]
                )
            }

        repository.insertAll(entities)
    }
}
