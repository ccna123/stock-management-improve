package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity

data class LocationChangeEventModel(
    val locationChangeEventId: Int = 0,
    val locationChangeSessionId: Int,
    val ledgerItemId: Int,
    val locationId: Int,
    val sourceEventId: String,
    val memo: String?,
    val scannedAt: String,
)

fun LocationChangeEventEntity.toModel() = LocationChangeEventModel(
    locationChangeEventId = locationChangeEventId,
    locationChangeSessionId = locationChangeSessionId,
    ledgerItemId = ledgerItemId,
    locationId = locationId,
    sourceEventId = sourceEventId,
    memo = memo,
    scannedAt = scannedAt
)

fun LocationChangeEventModel.toEntity() = LocationChangeEventEntity(
    locationChangeEventId = locationChangeEventId,
    locationChangeSessionId = locationChangeSessionId,
    ledgerItemId = ledgerItemId,
    locationId = locationId,
    sourceEventId = sourceEventId,
    memo = memo,
    scannedAt = scannedAt
)
