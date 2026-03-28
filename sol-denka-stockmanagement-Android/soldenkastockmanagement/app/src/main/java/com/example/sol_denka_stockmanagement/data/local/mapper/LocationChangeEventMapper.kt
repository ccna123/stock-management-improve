package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeEventEntity
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventModel

class LocationChangeEventMapper {

    fun com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeEventEntity.toModel() = LocationChangeEventModel(
        locationChangeEventId = locationChangeEventId,
        locationChangeSessionId = locationChangeSessionId,
        ledgerItemId = ledgerItemId,
        locationId = locationId,
        sourceEventId = sourceEventId,
        memo = memo,
        scannedAt = scannedAt
    )

    fun LocationChangeEventModel.toEntity() =
        _root_ide_package_.com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeEventEntity(
            locationChangeEventId = locationChangeEventId,
            locationChangeSessionId = locationChangeSessionId,
            ledgerItemId = ledgerItemId,
            locationId = locationId,
            sourceEventId = sourceEventId,
            memo = memo,
            scannedAt = scannedAt
        )
}