package com.example.sol_denka_stockmanagement.model.inbound

import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity

data class InboundSessionModel(
    val inboundSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)

fun InboundSessionEntity.toModel() = InboundSessionModel(
    inboundSessionId = inboundSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)

fun InboundSessionModel.toEntity() = InboundSessionEntity(
    inboundSessionId = inboundSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)
