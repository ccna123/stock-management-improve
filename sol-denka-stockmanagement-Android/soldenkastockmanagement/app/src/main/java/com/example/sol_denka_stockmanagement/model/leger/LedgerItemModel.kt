package com.example.sol_denka_stockmanagement.model.leger

import com.example.sol_denka_stockmanagement.database.entity.leger.LedgerItemEntity

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
    val misrollReason: String?,
    val createdAt: String,
    val updatedAt: String,
)

fun LedgerItemEntity.toModel() = LedgerItemModel(
    ledgerItemId, itemTypeId, locationId, isInStock,
    weight, grade, specificGravity, thickness, width,
    length, quantity, winderInfo, misrollReason,
    createdAt, updatedAt
)

fun LedgerItemModel.toEntity() = LedgerItemEntity(
    ledgerItemId, itemTypeId, locationId, isInStock,
    weight, grade, specificGravity, thickness, width,
    length, quantity, winderInfo, misrollReason,
    createdAt, updatedAt
)
