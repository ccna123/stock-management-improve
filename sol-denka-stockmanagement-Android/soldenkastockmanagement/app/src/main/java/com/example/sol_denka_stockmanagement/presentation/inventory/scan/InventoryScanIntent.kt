package com.example.sol_denka_stockmanagement.presentation.inventory.scan

import com.example.sol_denka_stockmanagement.constant.Tab

sealed interface InventoryScanIntent {
    data class ChangeTab(val tab: Tab) : InventoryScanIntent
    data class ToggleSelectionMode(val enabled: Boolean) : InventoryScanIntent
    data class ToggleDropDown(val show: Boolean) : InventoryScanIntent
}