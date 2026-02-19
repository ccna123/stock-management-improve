package com.example.sol_denka_stockmanagement.state

sealed interface DialogState {
    data object Hidden: DialogState

    data class Error(
        val message: String,
    ) : DialogState

    data class SaveCsvSuccessFailedSftp(
        val message: String,
    ) : DialogState

    data class SaveCsvSendSftpSuccess(
        val message: String,
    ) : DialogState

    data class SaveCsvFailed(
        val message: String,
    ) : DialogState

    data class Confirm(
        val message: String = "",
    ) : DialogState

    data class CancelOperation(
        val message: String = "",
    ) : DialogState

    data class SaveDataToDbFailed(
        val message: String = ""
    ): DialogState

    data class ExportCsvSuccess(
        val message: String = ""
    ): DialogState

    data class ExportCsvFailed(
        val message: String = ""
    ): DialogState

}