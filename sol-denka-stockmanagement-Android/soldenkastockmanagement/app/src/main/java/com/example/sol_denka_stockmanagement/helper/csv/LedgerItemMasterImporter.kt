package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel

class LedgerItemMasterImporter(
    private val repository: LedgerItemRepository
) : CsvImporter<LedgerItemModel>() {

    private fun parseNullableInt(v: String?): Int? {
        if (v == null) return null
        if (v.isBlank()) return null
        return v.toFloatOrNull()?.toInt()
    }

    private fun parseBoolean(v: String?): Boolean {
        if (v == null) return false
        return v == "1" || v.equals("true", ignoreCase = true)
    }

    override fun parse(lines: List<String>): List<LedgerItemModel> {
        return lines
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
    }

    override suspend fun persist(entities: List<LedgerItemModel>) {
        repository.replaceAll(entities)
    }
}
