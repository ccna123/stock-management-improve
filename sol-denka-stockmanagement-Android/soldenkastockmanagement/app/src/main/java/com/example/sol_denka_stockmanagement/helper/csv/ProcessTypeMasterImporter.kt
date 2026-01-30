package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel

class ProcessTypeMasterImporter(
    private val repository: ProcessTypeRepository,
): CsvImporter<ProcessTypeModel>() {

    override val requiredHeaders = setOf(
        "process_type_id",
        "process_code",
        "process_name"
    )

    override fun mapRow(row: CsvRow): ProcessTypeModel {
        return ProcessTypeModel(
            processTypeId = row.int("process_type_id")!!,
            processCode = row.string("process_code")!!,
            processName = row.string("process_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<ProcessTypeModel>) {
        repository.upsertAll(entities)
    }
}