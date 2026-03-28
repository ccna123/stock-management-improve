package com.example.sol_denka_stockmanagement.domain.model.location


data class LocationChangeEventModel(
    val locationChangeEventId: Int = 0,
    val locationChangeSessionId: Int,
    val ledgerItemId: Int,
    val locationId: Int,
    val sourceEventId: String,
    val memo: String?,
    val scannedAt: String,
)