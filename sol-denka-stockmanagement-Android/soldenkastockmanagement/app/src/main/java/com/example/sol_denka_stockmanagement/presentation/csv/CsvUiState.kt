package com.example.sol_denka_stockmanagement.presentation.csv

import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvFileInfoModel
import com.example.sol_denka_stockmanagement.domain.model.csv.ExportFileModel

data class CsvUiState(
    // csv type selection
    val csvType: String = "",
    val csvTypeExpanded: Boolean = false,
    // file transfer method
    val fileTransferMethodExpanded: Boolean = false,
    // import
    val csvFiles: List<CsvFileInfoModel> = emptyList(),
    val importFileSelectedIndex: Int = -1,
    val importFileSelectedName: String = "",
    val showProgress: Boolean = false,
    val importProgress: Float = 0f,
    val isImporting: Boolean = false,
    val importResultStatus: ProcessResult? = null,
    // export
    val exportFiles: List<ExportFileModel> = emptyList(),
    val exportFileSelectedIndex: Int = -1,
    val exportFileSessionId: Int = 0,
    val isExporting: Boolean = false,
    val exportResultStatus: ProcessResult? = null,
    // result dialog
    val showProcessResultDialog: Boolean = false,
    val processResultMessage: String? = null,
    // event → trigger global dialog
    val event: CsvEvent? = null
)
