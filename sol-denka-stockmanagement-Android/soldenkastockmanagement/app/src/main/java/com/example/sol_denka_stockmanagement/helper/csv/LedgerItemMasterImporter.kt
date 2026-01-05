package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel

class LedgerItemMasterImporter(
    private val repository: LedgerItemRepository,
    private val db: AppDatabase
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
            tagId = row.int("tag_id")!!,
            winderId = row.int("winder_id"),
            isInStock = row.boolean("is_in_stock"),
            weight = row.int("weight"),
            width = row.int("width"),
            length = row.int("length"),
            thickness = row.int("thickness"),
            lotNo = row.string("lot_no"),
            occurrenceReason = row.string("occurrence_reason"),
            quantity = row.int("quantity"),
            memo = row.string("memo"),
            occurredAt = row.string("occurred_at"),
            processedAt = row.string("processed_at"),
            registeredAt = row.string("registered_at")!!,
            updatedAt = row.string("updated_at")!!
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<LedgerItemModel>) {
        repository.replaceAll(entities)
    }
}

