package com.example.sol_denka_stockmanagement.model.outbound

import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity

data class OutBoundEventModel(
    val outboundEventId: Int = 0,
    val outboundSessionId: Int,
    val ledgerItemId: Int,
    val processTypeId: Int,
    val sourceEventId: String,
    val memo: String?,
    val processedAt: String?,
    val registeredAt: String,
)

fun OutBoundEventEntity.toModel() = OutBoundEventModel(
    outboundEventId = outboundEventId,
    outboundSessionId = outboundSessionId,
    ledgerItemId = ledgerItemId,
    processTypeId = processTypeId,
    sourceEventId = sourceEventId,
    memo = memo,
    processedAt = processedAt,
    registeredAt = registeredAt
)

fun OutBoundEventModel.toEntity() = OutBoundEventEntity(
    outboundEventId = outboundEventId,
    outboundSessionId = outboundSessionId,
    ledgerItemId = ledgerItemId,
    processTypeId = processTypeId,
    sourceEventId = sourceEventId,
    memo = memo,
    processedAt = processedAt,
    registeredAt = registeredAt
)