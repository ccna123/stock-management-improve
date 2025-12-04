package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity

data class LocationChangeSessionModel(
    val locationChangeSessionId: Int = 0,
    val deviceId: String,
    val executedAt: String,
)

fun LocationChangeSessionEntity.toModel() = LocationChangeSessionModel(
    locationChangeSessionId = locationChangeSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)

fun LocationChangeSessionModel.toEntity() = LocationChangeSessionEntity(
    locationChangeSessionId = locationChangeSessionId,
    deviceId = deviceId,
    executedAt = executedAt
)
