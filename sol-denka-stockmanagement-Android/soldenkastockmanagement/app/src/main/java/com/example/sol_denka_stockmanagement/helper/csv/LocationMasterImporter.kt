package com.example.sol_denka_stockmanagement.helper.csv

import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel

class LocationMasterImporter(
    private val repository: LocationMasterRepository,
    private val db: AppDatabase
) : CsvImporter<LocationMasterModel>() {

    override val requiredHeaders = setOf(
        "location_id",
        "location_code",
        "location_name",
        "memo"
    )

    override fun mapRow(row: CsvRow): LocationMasterModel {
        return LocationMasterModel(
            locationId = row.int("location_id")!!,
            locationCode = row.stringWithLength("location_code", min = 1, max = 50),
            locationName = row.stringWithLength("location_name", min = 1, max = 100)!!,
            memo = row.stringWithLength("memo", min = 1, max = 500)
        )
    }

    override suspend fun withTransaction(block: suspend () -> Unit) {
        db.withTransaction { block() }
    }

    override suspend fun replaceAllWithNewData(entities: List<LocationMasterModel>) {
        repository.replaceAll(entities)
    }
}
