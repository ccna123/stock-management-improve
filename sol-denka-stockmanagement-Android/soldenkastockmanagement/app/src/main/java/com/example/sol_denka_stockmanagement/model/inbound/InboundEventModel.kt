package com.example.sol_denka_stockmanagement.model.inbound

import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity

data class InboundEventModel(
    val inboundEventId: Int = 0,
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
    val missRollReason: String?,
    val memo: String?,
    val occurredAt: String,
    val registeredAt: String,
)

fun InboundEventEntity.toModel() = InboundEventModel(
    inboundEventId = inboundEventId,
    inboundSessionId = inboundSessionId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    tagId = tagId,
    weight = weight,
    grade = grade,
    specificGravity = specificGravity,
    thickness = thickness,
    width = width,
    length = length,
    quantity = quantity,
    winderInfo = winderInfo,
    missRollReason = missRollReason,
    memo = memo,
    occurredAt = occurredAt,
    registeredAt = registeredAt
)

fun InboundEventModel.toEntity() = InboundEventEntity(
    inboundEventId = inboundEventId,
    inboundSessionId = inboundSessionId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    tagId = tagId,
    weight = weight,
    grade = grade,
    specificGravity = specificGravity,
    thickness = thickness,
    width = width,
    length = length,
    quantity = quantity,
    winderInfo = winderInfo,
    missRollReason = missRollReason,
    memo = memo,
    occurredAt = occurredAt,
    registeredAt = registeredAt
)
