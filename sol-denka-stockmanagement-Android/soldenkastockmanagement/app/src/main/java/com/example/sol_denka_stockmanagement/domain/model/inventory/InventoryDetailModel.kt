package com.example.sol_denka_stockmanagement.domain.model.inventory

data class InventoryDetailModel(
    val inventoryDetailId: Int = 0,
    val inventorySessionId: Int,
    val ledgerItemId: Int,
    val tagId: Int,
    val scannedAt: String,
)
