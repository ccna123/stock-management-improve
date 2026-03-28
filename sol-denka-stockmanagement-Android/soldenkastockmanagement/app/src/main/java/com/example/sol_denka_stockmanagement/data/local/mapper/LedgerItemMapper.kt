package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.domain.model.ledger.LedgerItemModel

fun LedgerItemEntity.toModel() = LedgerItemModel(
    ledgerItemId = ledgerItemId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    tagId = tagId,
    winderId = winderInfoId,
    isInStock = isInStock,
    weight = weight,
    thickness = thickness,
    lotNo = lotNo,
    width = width,
    length = length,
    quantity = quantity,
    occurrenceReason = occurrenceReason,
    memo = memo,
    occurredAt = occurredAt,
    processedAt = processedAt,
    registeredAt = registeredAt,
    updatedAt = updatedAt
)

fun LedgerItemModel.toEntity() =
    LedgerItemEntity(
        ledgerItemId = ledgerItemId,
        itemTypeId = itemTypeId,
        locationId = locationId,
        tagId = tagId,
        winderInfoId = winderId,
        isInStock = isInStock,
        weight = weight,
        thickness = thickness,
        lotNo = lotNo,
        width = width,
        length = length,
        quantity = quantity,
        occurrenceReason = occurrenceReason,
        memo = memo,
        occurredAt = occurredAt,
        processedAt = processedAt,
        registeredAt = registeredAt,
        updatedAt = updatedAt
    )
