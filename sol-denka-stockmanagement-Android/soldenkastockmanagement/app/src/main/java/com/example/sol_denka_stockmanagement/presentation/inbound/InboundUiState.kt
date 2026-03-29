package com.example.sol_denka_stockmanagement.presentation.inbound

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

data class InboundUiState(
    val locationExpanded: Boolean = false,
    val categoryExpanded: Boolean = false,
    val winderExpanded: Boolean = false,
    val location: LocationMasterModel? = null,
    val weight: String = ""
)
