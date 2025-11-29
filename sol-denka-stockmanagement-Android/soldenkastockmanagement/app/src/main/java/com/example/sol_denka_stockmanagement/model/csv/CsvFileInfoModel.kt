package com.example.sol_denka_stockmanagement.model.csv

data class CsvFileInfoModel(
    val fileName: String,
    val fileSize: String,
    val progress: Float = 0f,
    val isCompleted: Boolean = false,
    val isFailed: Boolean = false,
)