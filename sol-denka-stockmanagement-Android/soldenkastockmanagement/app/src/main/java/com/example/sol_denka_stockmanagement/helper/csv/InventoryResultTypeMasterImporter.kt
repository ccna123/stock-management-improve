package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.model.inventory.InventoryResultTypeModel

class InventoryResultTypeMasterImporter(
    private val repository: InventoryResultTypeRepository,
    private val db: AppDatabase
): CsvImporter<InventoryResultTypeModel>() {

    override val requiredHeaders = setOf(
        "inventory_result_type_id",
        "inventory_result_code",
        "inventory_result_name",
    )

    override fun mapRow(row: CsvRow): InventoryResultTypeModel {
        return InventoryResultTypeModel(
            inventoryResultTypeId = row.int("inventory_result_type_id")!!,
            inventoryResultTypeCode = row.string("inventory_result_code")!!,
            inventoryResultTypeName = row.string("inventory_result_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<InventoryResultTypeModel>) {
        repository.replaceAll(entities)
    }
}