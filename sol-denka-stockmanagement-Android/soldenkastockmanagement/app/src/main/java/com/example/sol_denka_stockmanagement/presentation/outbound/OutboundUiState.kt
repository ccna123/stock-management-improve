package com.example.sol_denka_stockmanagement.presentation.outbound

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel

data class OutboundUiState(
    val handlingMethodExpanded: Boolean = false,
    val categoryExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    var isAllSelected: Boolean = false,
    val selectedChipIndex: Int = 0,
)
