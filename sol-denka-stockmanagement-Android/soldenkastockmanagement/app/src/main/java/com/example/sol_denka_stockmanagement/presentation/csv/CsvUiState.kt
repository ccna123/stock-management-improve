package com.example.sol_denka_stockmanagement.presentation.csv

data class CsvUiState(
    val fileTransferMethod: String = FileTransferMethod.WIFI.displayName,
    val csvTypeExpanded: Boolean = false,
    val fileTransferMethodExpanded: Boolean = false,
    val progress: Float = 0f,
    val isFileWorking: Boolean = false
)
