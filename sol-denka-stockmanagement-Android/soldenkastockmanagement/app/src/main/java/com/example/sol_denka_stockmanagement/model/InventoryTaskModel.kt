package com.tss.sol_bs_tirescan_app.model

import com.example.sol_denka_stockmanagement.database.entity.InventoryTaskEntity

data class InventoryTaskModel(
    val id: Int,
    val locationId: Int,
    val memo: String,
    val isExportCsv: Int,
    val executedAt: String
)

fun InventoryTaskModel.toInventoryTaskEntity(): InventoryTaskEntity{
    return InventoryTaskEntity(
        id = this.id,
        locationId = this.locationId,
        memo = this.memo,
        isExportCsv = this.isExportCsv,
        executedAt = this.executedAt
    )
}

fun InventoryTaskEntity.toInventoryTaskModel(): InventoryTaskModel{
    return InventoryTaskModel(
        id = this.id,
        locationId = this.locationId,
        memo = this.memo,
        isExportCsv = this.isExportCsv,
        executedAt = this.executedAt
    )
}
