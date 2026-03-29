package com.example.sol_denka_stockmanagement.presentation.outbound


data class OutboundUiState(
    val memo: String = "",
    val processedAtDate: String = "",
    val processedAtTime: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val isLoading: Boolean = false,
    val progress: Float = 0f,
    val event: OutboundEvent? = null
)
