package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel

class TagMasterImporter(
    private val repository: TagMasterRepository
) : CsvImporter<TagMasterModel>() {

    override fun parse(lines: List<String>): List<TagMasterModel> {
        return lines
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
    }

    override suspend fun persist(entities: List<TagMasterModel>) {
        repository.replaceAll(buffer)
    }
}
