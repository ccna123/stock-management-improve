package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity

data class LocationMasterModel(
    val locationId: Int,
    val locationName: String,
    val locationCode: String?,
)

fun LocationMasterEntity.toModel() = LocationMasterModel(
    locationId = locationId,
    locationCode = locationCode,
    locationName = locationName,
)

fun LocationMasterModel.toEntity() = LocationMasterEntity(
    locationId = locationId,
    locationCode = locationCode,
    locationName = locationName,
)
