package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel

class InboundEventMapper {
    fun InboundEventEntity.toModel() = InboundEventModel(
        inboundEventId = inboundEventId,
        inboundSessionId = inboundSessionId,
        itemTypeId = itemTypeId,
        locationId = locationId,
        winderId = winderId,
        tagId = tagId,
        sourceEventId = sourceEventId,
        weight = weight,
        width = width,
        length = length,
        thickness = thickness,
        lotNo = lotNo,
        occurrenceReason = occurrenceReason,
        quantity = quantity,
        memo = memo,
        occurredAt = occurredAt,
        processedAt = processedAt,
        registeredAt = registeredAt
    )

    fun InboundEventModel.toEntity() = InboundEventEntity(
        inboundEventId = inboundEventId,
        inboundSessionId = inboundSessionId,
        itemTypeId = itemTypeId,
        locationId = locationId,
        winderId = winderId,
        tagId = tagId,
        sourceEventId = sourceEventId,
        weight = weight,
        width = width,
        length = length,
        thickness = thickness,
        lotNo = lotNo,
        occurrenceReason = occurrenceReason,
        quantity = quantity,
        memo = memo,
        occurredAt = occurredAt,
        processedAt = processedAt,
        registeredAt = registeredAt
    )
}