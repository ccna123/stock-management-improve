package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.Tab
import java.time.LocalDate

data class AppSettingModel(
    var autoConnectChecked: Boolean = false,
    var isRfidVolumeOn: Boolean = false,
    var selectedTab: Tab = Tab.Left,
    val scanDataFrom: LocalDate? = null,
    val scanDataTo: LocalDate? = null,
    var autoConnectDeviceAddress: String = ""
)