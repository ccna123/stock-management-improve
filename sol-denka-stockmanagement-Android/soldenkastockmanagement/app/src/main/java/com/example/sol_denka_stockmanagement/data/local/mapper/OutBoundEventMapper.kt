package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutBoundEventEntity
import com.example.sol_denka_stockmanagement.domain.model.outbound.OutBoundEventModel

fun OutBoundEventEntity.toModel() = OutBoundEventModel(
    outboundEventId = outboundEventId,
    outboundSessionId = outboundSessionId,
    ledgerItemId = ledgerItemId,
    processTypeId = processTypeId,
    tagId = tagId,
    sourceEventId = sourceEventId,
    memo = memo,
    processedAt = processedAt,
    registeredAt = registeredAt
)

fun OutBoundEventModel.toEntity() =
    OutBoundEventEntity(
        outboundEventId = outboundEventId,
        outboundSessionId = outboundSessionId,
        ledgerItemId = ledgerItemId,
        processTypeId = processTypeId,
        tagId = tagId,
        sourceEventId = sourceEventId,
        memo = memo,
        processedAt = processedAt,
        registeredAt = registeredAt
    )
