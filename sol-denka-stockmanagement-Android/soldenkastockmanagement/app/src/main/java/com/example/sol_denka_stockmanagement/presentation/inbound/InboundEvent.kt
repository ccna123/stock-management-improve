package com.example.sol_denka_stockmanagement.presentation.inbound

sealed interface InboundEvent {
    data object SaveDbFailed : InboundEvent
    data object SaveCsvSuccess : InboundEvent
    data object SaveCsvFailed : InboundEvent
}