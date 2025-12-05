package com.example.sol_denka_stockmanagement.intent

sealed interface CsvIntent {
    data class ToggleFileSelect(val fileIndex: Int, val fileName: String): CsvIntent
    data class SelectCsvType(val csvType: String): CsvIntent
    data object ResetFileSelect: CsvIntent
}