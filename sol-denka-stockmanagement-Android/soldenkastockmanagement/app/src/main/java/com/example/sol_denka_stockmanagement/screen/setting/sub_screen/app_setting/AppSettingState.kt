package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import com.example.sol_denka_stockmanagement.constant.Tab
import java.time.LocalDate

data class AppSettingState(
    var autoConnectChecked: Boolean = false,
    var isRfidVolumeOn: Boolean = false,
    var selectedTab: Tab = Tab.Left,
    val scanDataFrom: LocalDate? = null,
    val scanDataTo: LocalDate? = null,
    val isDeleteSuccess: Boolean = false,
    var autoConnectDeviceAddress: String = ""
)