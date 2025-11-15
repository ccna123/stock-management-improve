package com.example.sol_denka_stockmanagement.intent

sealed class AppSettingIntent {
    data object ApplySettingChange : AppSettingIntent()
    data object HasAppSettingChangedWithoutSave : AppSettingIntent()
    data object LoadSettingFromFile : AppSettingIntent()
    data object DeleteScanTask : AppSettingIntent()
    data object ResetDeleteSuccess : AppSettingIntent()
    data class SetAutoConnect(
        val isAutoConnectSwitchOn: Boolean,
        val selectedDeviceAddress: String?
    ) : AppSettingIntent()

}