package com.example.sol_denka_stockmanagement.model.common

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagStatus

data class AdditionalFieldsModel(
    var tagStatus: TagStatus,
    var inventoryResultType: InventoryResultType,
    val rssi: Float

){
    companion object {
        fun default() = AdditionalFieldsModel(
            tagStatus = TagStatus.UNPROCESSED,
            inventoryResultType = InventoryResultType.UNKNOWN,
            rssi = -100f
        )
    }
}
