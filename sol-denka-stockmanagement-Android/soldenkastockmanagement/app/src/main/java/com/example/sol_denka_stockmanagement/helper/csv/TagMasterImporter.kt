package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel

class TagMasterImporter(
    private val repository: TagMasterRepository,
) : CsvImporter<TagMasterModel>() {

    override val requiredHeaders = setOf(
        "tag_id",
        "tag_status_id",
        "epc",
        "memo"
    )

    override fun mapRow(row: CsvRow): TagMasterModel {
        return TagMasterModel(
            tagId = row.int("tag_id")!!,
            tagStatusId = row.int("tag_status_id")!!,
            epc = row.stringWithExactLength("epc", length = 8)!!,
            memo = row.stringWithLength("memo", min = 1, max = 500),
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
    override suspend fun replaceAllWithNewData(entities: List<TagMasterModel>) {
        repository.upsertAll(entities)
    }
}

