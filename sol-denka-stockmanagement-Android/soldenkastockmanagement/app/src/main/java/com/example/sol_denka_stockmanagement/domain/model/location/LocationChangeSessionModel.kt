package com.example.sol_denka_stockmanagement.domain.model.location

data class LocationChangeSessionModel(
    val locationChangeSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)