package com.example.sol_denka_stockmanagement.presentation.location

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

data class LocationChangeUiState(
    val locationExpanded: Boolean = false,
    val location: LocationMasterModel? = null,
    val memo: String = "",
    val isLoading: Boolean = false,
    val progress: Float = 0f,
    val event: LocationChangeEvent? = null
)