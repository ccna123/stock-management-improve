package com.example.sol_denka_stockmanagement.model.ledger

import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity

data class LedgerItemModel(
    val ledgerItemId: Int,
    val itemTypeId: Int,
    val locationId: Int,
    val winderInfoId: Int?,
    val isInStock: Boolean,
    val weight: Int?,
    val width: Int?,
    val length: Int?,
    val thickness: Int?,
    val lotNo: String?,
    val occurrenceReason: String?,
    val quantity: Int?,
    val memo: String?,
    val occurredAt: String?,
    val processedAt: String?,
    val registeredAt: String,
    val updatedAt: String,
)

fun LedgerItemEntity.toModel() = LedgerItemModel(
    ledgerItemId = ledgerItemId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    winderInfoId = winderInfoId,
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

fun LedgerItemModel.toEntity() = LedgerItemEntity(
    ledgerItemId = ledgerItemId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    winderInfoId = winderInfoId,
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
