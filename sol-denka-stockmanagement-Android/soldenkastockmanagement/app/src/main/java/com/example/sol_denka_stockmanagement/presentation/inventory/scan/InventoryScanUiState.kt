package com.example.sol_denka_stockmanagement.presentation.inventory.scan

import com.example.sol_denka_stockmanagement.constant.Tab

data class InventoryScanUiState(
    val tab: Tab = Tab.Left,
    val isSelectionMode: Boolean = false,
    val showDropDown: Boolean = false
)