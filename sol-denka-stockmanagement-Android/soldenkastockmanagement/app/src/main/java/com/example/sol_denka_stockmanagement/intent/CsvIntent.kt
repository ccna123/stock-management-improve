package com.example.sol_denka_stockmanagement.intent

sealed interface CsvIntent {
    data class ToggleFileSelect(val fileIndex: Int): CsvIntent
    data object ResetFileSelect: CsvIntent
}