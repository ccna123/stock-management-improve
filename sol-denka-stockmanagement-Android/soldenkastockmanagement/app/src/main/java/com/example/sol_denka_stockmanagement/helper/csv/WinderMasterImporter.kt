package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderRepository
import com.example.sol_denka_stockmanagement.model.winder.WinderModel

class WinderMasterImporter(
    private val repository: WinderRepository,
) : CsvImporter<WinderModel>() {

    override val requiredHeaders = setOf(
        "winder_id",
        "winder_name",
    )

    override fun mapRow(row: CsvRow): WinderModel {
        return WinderModel(
            winderId = row.int("winder_id")!!,
            winderName = row.string("winder_name")!!,
            createdAt = generateTimeStamp(),
            updatedAt = generateTimeStamp()
        )
    }

    override suspend fun replaceAllWithNewData(entities: List<WinderModel>) {
        repository.upsertAll(entities)
    }
}