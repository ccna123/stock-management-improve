package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

class LocationMasterMapper {

    fun LocationMasterEntity.toModel() = LocationMasterModel(
        locationId = locationId,
        locationCode = locationCode,
        locationName = locationName,
        memo = memo
    )

    fun LocationMasterModel.toEntity() = LocationMasterEntity(
        locationId = locationId,
        locationCode = locationCode,
        locationName = locationName,
        memo = memo
    )
}