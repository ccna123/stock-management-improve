package com.example.sol_denka_stockmanagement.domain.model.location

data class LocationMasterModel(
    val locationId: Int,
    val locationName: String,
    val locationCode: String?,
    val memo: String?
)