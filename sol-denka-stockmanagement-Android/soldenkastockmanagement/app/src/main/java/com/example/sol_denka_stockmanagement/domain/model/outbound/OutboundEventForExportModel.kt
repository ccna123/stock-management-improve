package com.example.sol_denka_stockmanagement.domain.model.outbound

data class OutboundEventForExportModel(
    val ledgerItemId: Int,
    val tagId: Int,
    val processTypeId: Int,
    val deviceId: String,
    val sourceEventId: String,
    val memo: String?,
    val processedAt: String?,
    val registeredAt: String,
)