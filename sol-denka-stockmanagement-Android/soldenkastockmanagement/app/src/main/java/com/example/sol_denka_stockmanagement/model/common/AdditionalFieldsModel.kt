package com.example.sol_denka_stockmanagement.model.common

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagStatus

data class AdditionalFieldsModel(
    var tagStatus: TagStatus,
    var inventoryResultType: InventoryResultType,
    val rssi: Float,
    val itemName: String,
    val itemCode: String,
    val location: String,
    val processType: String,
    val isChecked: Boolean,
    val hasLeger: Boolean


){
    companion object {
        fun default() = AdditionalFieldsModel(
            tagStatus = TagStatus.UNPROCESSED,
            inventoryResultType = InventoryResultType.UNKNOWN,
            rssi = -100f,
            itemName = "",
            itemCode = "",
            location = "",
            processType = "",
            isChecked = false,
            hasLeger = false
        )
    }
}
