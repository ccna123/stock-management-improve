package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel

fun InboundSessionEntity.toModel() =
    InboundSessionModel(
        inboundSessionId = inboundSessionId,
        deviceId = deviceId,
        executedAt = executedAt
    )

fun InboundSessionModel.toEntity() =
    InboundSessionEntity(
        inboundSessionId = inboundSessionId,
        deviceId = deviceId,
        executedAt = executedAt
    )
