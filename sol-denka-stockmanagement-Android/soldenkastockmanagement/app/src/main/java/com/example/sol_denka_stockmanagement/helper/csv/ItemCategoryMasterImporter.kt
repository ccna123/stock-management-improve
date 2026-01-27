package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.item.ItemCategoryRepository
import com.example.sol_denka_stockmanagement.model.item.ItemCategoryModel

class ItemCategoryMasterImporter(
    private val repository: ItemCategoryRepository,
    private val db: AppDatabase
): CsvImporter<ItemCategoryModel>() {

    override val requiredHeaders = setOf(
        "item_category_id",
        "item_category_name",
    )

    override fun mapRow(row: CsvRow): ItemCategoryModel {
        return ItemCategoryModel(
            itemCategoryId = row.int("item_category_id")!!,
            itemCategoryName = row.string("item_category_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp(),
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<ItemCategoryModel>) {
        repository.replaceAll(entities)
    }
}