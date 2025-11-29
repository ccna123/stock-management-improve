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
    locationId,
    locationCode,
    locationName,
    createdAt,
    updatedAt
)

fun LocationMasterModel.toEntity() = LocationMasterEntity(
    locationId,
    locationCode,
    locationName,
    createdAt,
    updatedAt
)
