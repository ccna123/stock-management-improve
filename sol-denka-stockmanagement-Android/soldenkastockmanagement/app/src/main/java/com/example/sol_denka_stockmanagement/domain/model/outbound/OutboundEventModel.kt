package com.example.sol_denka_stockmanagement.domain.model.outbound

data class OutBoundEventModel(
    val outboundEventId: Int = 0,
    val outboundSessionId: Int,
    val ledgerItemId: Int,
    val processTypeId: Int,
    val tagId: Int,
    val sourceEventId: String,
    val memo: String?,
    val processedAt: String?,
    val registeredAt: String,
)