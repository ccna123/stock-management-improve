package com.example.sol_denka_stockmanagement.helper.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel

class LocationMasterImporter(
    private val repository: LocationRepository
): ICsvImport {

    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1) // skip header
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
                    createdAt = generateTimeStamp(),
                    updatedAt = generateTimeStamp()
                )
            }

        repository.insertAll(entities)
    }
}