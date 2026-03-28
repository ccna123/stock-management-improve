package com.example.sol_denka_stockmanagement.domain.model.inventory

data class InventorySessionModel(
    val inventorySessionId: Int = 0,
    val sourceSessionUuid: String,
    val locationId: Int,
    val memo: String?,
    val deviceId: String,
    val executedAt: String,
)