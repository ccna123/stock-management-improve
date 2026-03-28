package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeSessionModel

class LocationChangeSessionMapper {

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
}