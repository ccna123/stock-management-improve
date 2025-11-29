package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity

data class LocationChangeSessionModel(
    val locationChangeSessionId: Int,
    val deviceId: String,
    val executedAt: String,
)

fun LocationChangeSessionEntity.toModel() = LocationChangeSessionModel(
    locationChangeSessionId,
    deviceId,
    executedAt
)

fun LocationChangeSessionModel.toEntity() = LocationChangeSessionEntity(
    locationChangeSessionId,
    deviceId,
    executedAt
)
