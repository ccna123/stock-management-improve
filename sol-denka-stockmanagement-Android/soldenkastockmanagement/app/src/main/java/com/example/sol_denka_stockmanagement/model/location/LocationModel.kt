package com.example.sol_denka_stockmanagement.model.location

import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity

data class LocationMasterModel(
    val locationId: Int,
    val locationCode: String?,
    val locationName: String?,
    val createdAt: String,
    val updatedAt: String,
)

fun LocationMasterEntity.toModel() = LocationMasterModel(
    locationId = locationId,
    locationCode = locationCode,
    locationName = locationName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun LocationMasterModel.toEntity() = LocationMasterEntity(
    locationId = locationId,
    locationCode = locationCode,
    locationName = locationName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
