package com.example.sol_denka_stockmanagement.presentation.inventory.input

sealed interface InventoryIntent {
    data class LocationChanged(val location: LocationMasterModel?) : InventoryIntent
    data object ToggleLocationExpanded : InventoryIntent
    data class MemoChanged(val value: String) : InventoryIntent
}