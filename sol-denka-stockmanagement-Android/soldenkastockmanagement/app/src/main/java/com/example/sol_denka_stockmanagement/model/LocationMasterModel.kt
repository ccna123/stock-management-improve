package com.tss.sol_bs_tirescan_app.model

import com.example.sol_denka_stockmanagement.database.entity.LocationMasterEntity

data class LocationMasterModel(
    val id: Int,
    val locationCode: String,
    val locationName: String,
    val createdAt: String,
    val updatedAt: String
)

fun LocationMasterModel.toLocationMasterEntity(): LocationMasterEntity {
    return LocationMasterEntity(
        id = id,
        locationCode = locationCode,
        locationName = locationName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun LocationMasterEntity.toLocationMasterModel(): LocationMasterModel {
    return LocationMasterModel(
        id = id,
        locationCode = locationCode,
        locationName = locationName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
