package com.example.sol_denka_stockmanagement.presentation.outbound

sealed interface OutboundEvent {
    data object SaveDbFailed : OutboundEvent
    data object SaveCsvSuccess : OutboundEvent
    data object SaveCsvFailed : OutboundEvent
}