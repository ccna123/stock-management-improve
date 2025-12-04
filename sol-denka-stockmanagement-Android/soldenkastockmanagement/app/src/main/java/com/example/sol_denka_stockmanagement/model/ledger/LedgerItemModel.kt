package com.example.sol_denka_stockmanagement.model.ledger

import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity

data class LedgerItemModel(
    val ledgerItemId: Int,
    val itemTypeId: Int,
    val locationId: Int,
    val isInStock: Boolean,
    val weight: Int?,
    val grade: String?,
    val specificGravity: Int?,
    val thickness: Int?,
    val width: Int?,
    val length: Int?,
    val quantity: Int?,
    val winderInfo: String?,
    val missRollReason: String?,
    val createdAt: String,
    val updatedAt: String,
)

fun LedgerItemEntity.toModel() = LedgerItemModel(
    ledgerItemId = ledgerItemId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    isInStock = isInStock,
    weight = weight,
    grade = grade,
    specificGravity = specificGravity,
    thickness = thickness,
    width = width,
    length = length,
    quantity = quantity,
    winderInfo = winderInfo,
    missRollReason = missRollReason,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun LedgerItemModel.toEntity() = LedgerItemEntity(
    ledgerItemId = ledgerItemId,
    itemTypeId = itemTypeId,
    locationId = locationId,
    isInStock = isInStock,
    weight = weight,
    grade = grade,
    specificGravity = specificGravity,
    thickness = thickness,
    width = width,
    length = length,
    quantity = quantity,
    winderInfo = winderInfo,
    missRollReason = missRollReason,
    createdAt = createdAt,
    updatedAt = updatedAt
)
