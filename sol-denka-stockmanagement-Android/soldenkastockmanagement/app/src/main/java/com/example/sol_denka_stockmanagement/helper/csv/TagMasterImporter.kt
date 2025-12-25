package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.app_interface.ICsvImport
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel

class TagMasterImporter(
    private val repository: TagMasterRepository
) : ICsvImport {

    private val buffer = mutableListOf<TagMasterModel>()

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
                    tagStatusId = p[1].toInt(),
                    epc = p[2],
                    newFields = AdditionalFieldsModel(
                        tagScanStatus = TagScanStatus.UNPROCESSED,
                        inventoryResultType = InventoryResultType.UNKNOWN,
                        rssi = -100f,
                        itemName = "",
                        itemCode = "",
                        location = "",
                        processType = "",
                        isChecked = false,
                        hasLeger = false,
                        isInStock = false
                    )
                )
            }
        buffer.addAll(entities)
    }

    override suspend fun finish() {
        if (buffer.isEmpty()) return

        repository.replaceAll(buffer)
        buffer.clear()
    }
}
