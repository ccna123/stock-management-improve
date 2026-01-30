package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel
import java.math.BigDecimal

class LedgerItemMasterImporter(
    private val repository: LedgerItemRepository,
) : CsvImporter<LedgerItemModel>() {

    override val requiredHeaders = setOf(
        "ledger_item_id",
        "item_type_id",
        "location_id",
        "tag_id",
        "winder_id",
        "is_in_stock",
        "weight",
        "width",
        "length",
        "thickness",
        "lot_no",
        "occurrence_reason",
        "quantity",
        "memo",
        "occurred_at",
        "processed_at",
        "registered_at",
        "updated_at"
    )

    override fun mapRow(row: CsvRow): LedgerItemModel {
        return LedgerItemModel(
            ledgerItemId = row.int("ledger_item_id")!!,
            itemTypeId = row.int("item_type_id")!!,
            locationId = row.int("location_id")!!,
            tagId = row.int("tag_id"),
            winderId = row.int("winder_id"),
            isInStock = row.boolean("is_in_stock"),
            weight = row.intRange("weight", min = 0, max = 9999),
            width = row.intRange("width", min = 0, max = 9999),
            length = row.intRange("length", min = 0, max = 9999),
            thickness = row.decimal3("thickness", min = BigDecimal("0.000"), max = BigDecimal("999.999")),
            lotNo = row.stringWithLength("lot_no", min = 1, max = 32),
            occurrenceReason = row.stringWithLength("occurrence_reason", min = 1, max = 100),
            quantity = row.intRange("quantity", min = 0, max = 999999),
            memo = row.stringWithLength("memo", min = 1, max = 500),
            occurredAt = row.string("occurred_at"),
            processedAt = row.string("processed_at"),
            registeredAt = row.string("registered_at")!!,
            updatedAt = row.string("updated_at")!!
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<LedgerItemModel>) {
        repository.upsertAll(entities)
    }
}

