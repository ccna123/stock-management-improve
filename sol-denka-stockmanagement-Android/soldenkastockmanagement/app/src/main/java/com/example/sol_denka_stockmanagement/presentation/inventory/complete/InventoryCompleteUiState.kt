package com.example.sol_denka_stockmanagement.presentation.inventory.complete

data class InventoryCompleteUiState(
    val memo: String = "",
    val okCount: Int = 0,
    val shortageCount: Int = 0,
    val overCount: Int = 0,
    val wrongLocationCount: Int = 0,
    val isLoading: Boolean = false,
    val progress: Float = 0f,
    val event: InventoryCompleteEvent? = null
)