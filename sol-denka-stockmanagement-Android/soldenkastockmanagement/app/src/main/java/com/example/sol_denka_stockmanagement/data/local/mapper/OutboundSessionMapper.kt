package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutboundSessionModel

class OutboundSessionMapper {

    fun com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity.toModel() = OutboundSessionModel(
        outboundSessionId = outboundSessionId,
        deviceId = deviceId,
        executedAt = executedAt
    )

    fun OutboundSessionModel.toEntity() =
        _root_ide_package_.com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity(
            outboundSessionId = outboundSessionId,
            deviceId = deviceId,
            executedAt = executedAt
        )
}