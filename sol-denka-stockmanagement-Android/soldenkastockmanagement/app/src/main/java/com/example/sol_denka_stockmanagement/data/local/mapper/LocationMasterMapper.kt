package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.location.LocationMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

fun LocationMasterEntity.toModel() = LocationMasterModel(
    locationId = locationId,
    locationCode = locationCode,
    locationName = locationName,
    memo = memo
)

fun LocationMasterModel.toEntity() =
    LocationMasterEntity(
        locationId = locationId,
        locationCode = locationCode,
        locationName = locationName,
        memo = memo
    )