package com.example.sol_denka_stockmanagement.helper.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.leger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.model.leger.LedgerItemModel

class LedgerItemMasterImporter(
    private val repository: LedgerItemRepository
): ICsvImport {

    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1) // skip header
            .map { it.trim() } // remove whitespace at start and end
            .filter { it.isNotEmpty() } // skip blank row or empty value
            .map { line -> line.split(",") }
            .map { p ->
                LedgerItemModel(
                    ledgerItemId = p[0].toInt(),
                    itemTypeId = p[1].toInt(),
                    locationId = p[2].toInt(),
                    isInStock = p[3].toBoolean(),
                    weight = p[4].toInt(),
                    grade = p[5],
                    specificGravity = p[6].toInt(),
                    thickness = p[7].toInt(),
                    width = p[8].toInt(),
                    length = p[9].toInt(),
                    quantity = p[10].toInt(),
                    winderInfo = p[11],
                    misrollReason = p[12],
                    createdAt = generateTimeStamp(),
                    updatedAt = generateTimeStamp()
                )
            }
        Log.e("TSS", "entities: ${entities}")

        repository.insertAll(entities)
    }
}