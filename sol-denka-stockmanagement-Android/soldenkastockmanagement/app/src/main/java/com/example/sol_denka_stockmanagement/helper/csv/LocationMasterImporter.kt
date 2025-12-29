package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel

class LocationMasterImporter(
    private val repository: LocationMasterRepository
) : CsvImporter<LocationMasterModel>() {

    override fun parse(lines: List<String>): List<LocationMasterModel> {
        return lines
            .map { it.trim() } // remove whitespace at start and end
            .filter { it.isNotEmpty() } // skip blank row or empty value
            .map { line -> line.split(",") }
            .filter { p ->
                p.size == 3 && p.any { col -> col.isNotBlank() } // guarantee number of columns
            }
            .map { p ->
                LocationMasterModel(
                    locationId = p[0].toInt(),
                    locationCode = p[1],
                    locationName = p[2],
                )
            }
    }

    override suspend fun persist(entities: List<LocationMasterModel>) {
        repository.replaceAll(buffer)
    }
}