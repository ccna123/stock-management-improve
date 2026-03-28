package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel


fun OutboundSessionEntity.toModel() = OutboundSessionModel(
    outboundSessionId = outboundSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)

fun OutboundSessionModel.toEntity() =
    OutboundSessionEntity(
        outboundSessionId = outboundSessionId,
        deviceId = deviceId,
        executedAt = executedAt
    )
