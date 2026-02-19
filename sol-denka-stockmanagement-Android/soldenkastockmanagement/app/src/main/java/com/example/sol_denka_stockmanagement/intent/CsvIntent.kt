package com.example.sol_denka_stockmanagement.intent

sealed interface CsvIntent {
    data class ToggleFileSelect(val fileIndex: Int, val fileName: String, val type: String): CsvIntent
    data class SelectCsvType(val csvType: String): CsvIntent
    data class ToggleProgressVisibility(val show: Boolean): CsvIntent
    data object ResetFileSelect: CsvIntent
    data object ResetFileSelectedStatus: CsvIntent
    data object ClearCsvFileList: CsvIntent
    data object FetchCsvFiles: CsvIntent
    data object FetchExportCsvFiles: CsvIntent
    data object ResetCsvType: CsvIntent
}