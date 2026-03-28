package com.example.sol_denka_stockmanagement.domain.model.outbound


data class OutboundSessionModel(
    val outboundSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)
