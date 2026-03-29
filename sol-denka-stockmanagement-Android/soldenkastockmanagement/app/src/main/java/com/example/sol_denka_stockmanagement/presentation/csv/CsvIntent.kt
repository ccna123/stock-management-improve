package com.example.sol_denka_stockmanagement.presentation.csv

sealed interface CsvIntent {
    // csv type
    data class SelectCsvType(val csvType: String) : CsvIntent
    data object ResetCsvType : CsvIntent
    data object ToggleCsvTypeExpanded : CsvIntent
    // file select
    data class ToggleFileSelect(
        val type: String,
        val fileIndex: Int,
        val fileName: String = "",
        val fileSessionId: Int = 0
    ) : CsvIntent
    data object ResetFileSelect : CsvIntent
    data object ResetFileSelectedStatus : CsvIntent
    // progress
    data class ToggleProgressVisibility(val show: Boolean) : CsvIntent
    // list files
    data object FetchCsvFiles : CsvIntent
    data object FetchExportCsvFiles : CsvIntent
    data object ClearCsvFileList : CsvIntent
    // actions
    data object ImportMaster : CsvIntent
    data class ExportCsv(val sessionId: Int, val csvType: String) : CsvIntent
    // dialog
    data object DismissProcessResultDialog : CsvIntent
    // event consumed
    data object EventConsumed : CsvIntent
}