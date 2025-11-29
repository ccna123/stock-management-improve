package com.example.sol_denka_stockmanagement.model.inbound

import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity

data class InboundEventModel(
    val inboundEventId: Int,
    val inboundSessionId: Int,
    val itemTypeId: Int,
    val locationId: Int,
    val tagId: Int?,
    val weight: Int?,
    val grade: String?,
    val specificGravity: String?,
    val thickness: Int?,
    val width: Int?,
    val length: Int?,
    val quantity: Int?,
    val winderInfo: String?,
    val misrollReason: String?,
    val memo: String?,
    val occurredAt: String,
    val registeredAt: String,
)

fun InboundEventEntity.toModel() = InboundEventModel(
    inboundEventId, inboundSessionId, itemTypeId, locationId, tagId,
    weight, grade, specificGravity, thickness, width, length,
    quantity, winderInfo, misrollReason, memo, occurredAt, registeredAt
)

fun InboundEventModel.toEntity() = InboundEventEntity(
    inboundEventId, inboundSessionId, itemTypeId, locationId, tagId,
    weight, grade, specificGravity, thickness, width, length,
    quantity, winderInfo, misrollReason, memo, occurredAt, registeredAt
)
