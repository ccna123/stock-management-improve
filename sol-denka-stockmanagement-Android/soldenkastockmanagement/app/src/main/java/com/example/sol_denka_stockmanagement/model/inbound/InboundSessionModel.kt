package com.example.sol_denka_stockmanagement.model.inbound

import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity

data class InboundSessionModel(
    val inboundSessionId: Int,
    val deviceId: String,
    val executedAt: String,
)

fun InboundSessionEntity.toModel() = InboundSessionModel(
    inboundSessionId, deviceId, executedAt
)

fun InboundSessionModel.toEntity() = InboundSessionEntity(
    inboundSessionId, deviceId, executedAt
)
