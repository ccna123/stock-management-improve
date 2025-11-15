package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel

sealed class InventoryScanIntent {
    data class ToggleSelectionMode(val selectionMode: Boolean): InventoryScanIntent()
    data class ToggleTagSelection(val item: InventoryItemMasterModel): InventoryScanIntent()
    data object ClearTagSelectionList: InventoryScanIntent()
}