package com.example.sol_denka_stockmanagement.presentation.location

sealed interface LocationChangeEvent {
    data object SaveDbFailed : LocationChangeEvent
    data object SaveCsvSuccess : LocationChangeEvent
    data object SaveCsvFailed : LocationChangeEvent
}