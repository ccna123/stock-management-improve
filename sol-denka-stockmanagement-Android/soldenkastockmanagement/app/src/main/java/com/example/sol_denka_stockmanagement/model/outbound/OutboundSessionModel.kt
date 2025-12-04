package com.example.sol_denka_stockmanagement.model.outbound

import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity

data class OutboundSessionModel(
    val outboundSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)

fun OutboundSessionEntity.toModel() = OutboundSessionModel(
    outboundSessionId = outboundSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)

fun OutboundSessionModel.toEntity() = OutboundSessionEntity(
    outboundSessionId = outboundSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)
