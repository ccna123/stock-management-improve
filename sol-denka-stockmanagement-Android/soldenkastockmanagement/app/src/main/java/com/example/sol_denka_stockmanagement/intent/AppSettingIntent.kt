package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod

sealed interface AppSettingIntent {
    data class ChangeFileTransferMethod(val method: FileTransferMethod): AppSettingIntent
}