package com.example.sol_denka_stockmanagement.presentation.csv

sealed interface CsvEvent {
    data object ExportSuccess : CsvEvent
    data object ExportFailed : CsvEvent
}