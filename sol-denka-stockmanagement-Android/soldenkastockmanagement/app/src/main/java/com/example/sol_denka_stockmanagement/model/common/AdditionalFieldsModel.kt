package com.example.sol_denka_stockmanagement.model.common

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus

data class AdditionalFieldsModel(
    var tagScanStatus: TagScanStatus,
    var inventoryResultType: InventoryResultType,
    val rssi: Float,
    val itemName: String,
    val itemCode: String,
    val location: String,
    val processType: String,
    val isChecked: Boolean,
    val hasLeger: Boolean,
    val isInStock: Boolean


){
    companion object {
        fun default() = AdditionalFieldsModel(
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
    }
}
