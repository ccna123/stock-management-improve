package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.model.csv.CsvTaskTypeModel

class CsvTaskTypeMasterImporter(
    private val repository: CsvTaskTypeRepository,
    private val db: AppDatabase
)  : CsvImporter<CsvTaskTypeModel>() {

    override val requiredHeaders = setOf(
        "csv_task_type_id",
        "csv_task_code",
        "csv_task_name"
    )

    override fun mapRow(row: CsvRow): CsvTaskTypeModel {
        return CsvTaskTypeModel(
            csvTaskTypeId = row.int("csv_task_type_id")!!,
            csvTaskCode = row.string("csv_task_code")!!,
            csvTaskName = row.string("csv_task_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<CsvTaskTypeModel>) {
        repository.replaceAll(entities)
    }
}