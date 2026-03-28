package com.example.sol_denka_stockmanagement.domain.model.inbound

data class InboundSessionModel(
    val inboundSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)