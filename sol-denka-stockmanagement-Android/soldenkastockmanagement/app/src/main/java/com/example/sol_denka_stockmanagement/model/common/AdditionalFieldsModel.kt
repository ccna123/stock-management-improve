package com.example.sol_denka_stockmanagement.model.common

import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import java.math.BigDecimal

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
    val isInStock: Boolean,
    val readTimeStamp: Long,
    val packingType: String?,
    val specificGravity: Int?,
    val thickness: BigDecimal?,
    val length: Int?,
    val width: Int?,
    val weight: Int?,
    val lotNo: String?,
    val quantity: Int?,
    val occurrenceReason: String?,
    val occurredAt: String?,
    val processedAt: String?,
    val winderName: String?,
    val categoryName: String,
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
            isInStock = false,
            readTimeStamp = 0,
            packingType = "",
            specificGravity = 0,
            thickness = BigDecimal.ZERO,
            length = 0,
            width = 0,
            weight = 0,
            lotNo = "",
            quantity = 0,
            occurrenceReason = "",
            occurredAt = "",
            processedAt = "",
            winderName = "",
            categoryName = ""
        )
    }
}
