package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.intent.AppSettingIntent
import java.time.LocalDate

data class AppSettingState(
    var fileTransferMethod: FileTransferMethod = FileTransferMethod.SELECTION_TITLE
)