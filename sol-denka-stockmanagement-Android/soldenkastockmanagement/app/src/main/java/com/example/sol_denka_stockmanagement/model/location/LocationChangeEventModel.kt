package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity

data class LocationChangeEventModel(
    val locationChangeEventId: Int,
    val locationChangeSessionId: Int,
    val ledgerItemId: Int,
    val locationId: Int,
    val memo: String?,
    val scannedAt: String,
)

fun LocationChangeEventEntity.toModel() = LocationChangeEventModel(
    locationChangeEventId,
    locationChangeSessionId,
    ledgerItemId,
    locationId,
    memo,
    scannedAt
)

fun LocationChangeEventModel.toEntity() = LocationChangeEventEntity(
    locationChangeEventId,
    locationChangeSessionId,
    ledgerItemId,
    locationId,
    memo,
    scannedAt
)
