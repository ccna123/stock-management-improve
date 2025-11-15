package com.example.sol_denka_stockmanagement.screen.csv

data class CsvState(
    val csvTypeExpanded: Boolean = false,
    val tranActionExpanded: Boolean = false,
    var isSaveSuccess: Boolean = false,
    var csvType: String = "",
    val currentSelectedCsvIndex: Int = 0,
    val isSelectionMode: Boolean = false,
    val hasExportBeenAttempted: Boolean = false
)
