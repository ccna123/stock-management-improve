package com.example.sol_denka_stockmanagement.presentation.inventory.complete

sealed interface InventoryCompleteEvent {
    data object SaveDbFailed : InventoryCompleteEvent
    data object SaveCsvSuccess : InventoryCompleteEvent
    data object SaveCsvFailed : InventoryCompleteEvent
}