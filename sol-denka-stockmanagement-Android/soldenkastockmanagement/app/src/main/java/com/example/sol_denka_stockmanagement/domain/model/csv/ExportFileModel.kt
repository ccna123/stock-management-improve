package com.example.sol_denka_stockmanagement.domain.model.csv

data class ExportFileModel(
    val sessionId: Int,
    val timeStamp: String,
    val fileName: String,
)
