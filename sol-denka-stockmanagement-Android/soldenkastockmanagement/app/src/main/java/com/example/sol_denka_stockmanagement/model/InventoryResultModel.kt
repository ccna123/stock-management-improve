package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.InventoryResultCode
import com.example.sol_denka_stockmanagement.database.entity.InventoryResultEntity

data class InventoryResultModel(
    val id: Int,
    val taskId: Int,
    val itemId: Int,
    val epc: String,
    val scanResultType: InventoryResultCode,
    val scannedAt: String
)

fun InventoryResultModel.toInventoryResultEntity(): InventoryResultEntity {
    return InventoryResultEntity(
        id = this.id,
        taskId = this.taskId,
        itemId = this.itemId,
        epc = this.epc,
        scanResultType = this.scanResultType,
        scannedAt = this.scannedAt
    )
}

fun InventoryResultEntity.toInventoryResultModel(): InventoryResultModel {
    return InventoryResultModel(
        id = this.id,
        taskId = this.taskId,
        itemId = this.itemId,
        epc = this.epc,
        scanResultType = this.scanResultType,
        scannedAt = this.scannedAt
    )
}
