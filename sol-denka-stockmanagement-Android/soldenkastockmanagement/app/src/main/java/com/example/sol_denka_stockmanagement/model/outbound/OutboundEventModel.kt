package com.example.sol_denka_stockmanagement.model.outbound

import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity

data class OutBoundEventModel(
    val outboundEventId: Int,
    val outboundSessionId: Int,
    val ledgerItemId: Int,
    val processTypeId: Int,
    val memo: String?,
    val occurredAt: String,
    val registeredAt: String,
)

fun OutBoundEventEntity.toModel() = OutBoundEventModel(
    outboundEventId = outboundEventId,
    outboundSessionId = outboundSessionId,
    ledgerItemId = ledgerItemId,
    processTypeId = processTypeId,
    memo = memo,
    occurredAt = occurredAt,
    registeredAt = registeredAt
)

fun OutBoundEventModel.toEntity() = OutBoundEventEntity(
    outboundEventId = outboundEventId,
    outboundSessionId = outboundSessionId,
    ledgerItemId = ledgerItemId,
    processTypeId = processTypeId,
    memo = memo,
    occurredAt = occurredAt,
    registeredAt = registeredAt
)
