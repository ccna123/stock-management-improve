package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.tag.TagStatusMasterRepository
import com.example.sol_denka_stockmanagement.model.tag.TagStatusMasterModel

class TagStatusMasterImporter(
    private val repository: TagStatusMasterRepository,
    private val db: AppDatabase
): CsvImporter<TagStatusMasterModel>() {

    override val requiredHeaders = setOf(
        "tag_status_id",
        "status_code",
        "status_name"
    )

    override fun mapRow(row: CsvRow): TagStatusMasterModel {
        return TagStatusMasterModel(
            tagStatusId = row.int("tag_status_id")!!,
            statusCode = row.string("status_code")!!,
            statusName = row.string("status_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<TagStatusMasterModel>) {
        repository.replaceAll(entities)
    }
}