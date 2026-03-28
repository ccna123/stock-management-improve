package com.example.sol_denka_stockmanagement.domain.model.ledger

import java.math.BigDecimal

data class LedgerItemModel(
    val ledgerItemId: Int,
    val itemTypeId: Int,
    val locationId: Int,
    val winderId: Int?,
    val tagId: Int?,
    val isInStock: Boolean,
    val weight: Int?,
    val width: Int?,
    val length: Int?,
    val thickness: BigDecimal?,
    val lotNo: String?,
    val occurrenceReason: String?,
    val quantity: Int?,
    val memo: String?,
    val occurredAt: String?,
    val processedAt: String?,
    val registeredAt: String,
    val updatedAt: String,
)
