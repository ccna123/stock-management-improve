package com.example.sol_denka_stockmanagement.presentation.inventory.input

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

data class InventoryUiState(
    val location: LocationMasterModel? = null,
    val locationExpanded: Boolean = false,
    val memo: String = ""
)