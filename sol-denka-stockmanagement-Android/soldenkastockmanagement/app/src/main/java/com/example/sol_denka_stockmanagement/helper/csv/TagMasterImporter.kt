package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel

class TagMasterImporter(
    private val repository: TagMasterRepository
): ICsvImport {
    override suspend fun import(csvLines: List<String>) {
        if (csvLines.isEmpty()) return

        val entities = csvLines
            .drop(1)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",") }
            .map { p ->
                TagMasterModel(
                    tagId = p[0].toInt(),
                    ledgerItemId = p[1].ifBlank { null }?.toIntOrNull(),
                    epc = p[2],
                    createdAt = generateTimeStamp(),
                    updatedAt = generateTimeStamp(),
                    newFields = AdditionalFieldsModel(
                        tagStatus = TagStatus.UNPROCESSED,
                        inventoryResultType = InventoryResultType.UNKNOWN,
                        rssi = -100f
                    )
                )
            }

        repository.insertAll(entities)
    }
}
