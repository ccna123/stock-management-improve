package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.constant.BeeperVolume
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.SESSION

sealed interface ReaderSettingIntent {
    data class ChangeVolume(val newValue: BeeperVolume): ReaderSettingIntent
    data class ChangeRadioPower(val newValue: Int): ReaderSettingIntent
    data class ChangeSession(val newValue: SESSION): ReaderSettingIntent
    data class ChangeTagPopulation(val newValue: Short): ReaderSettingIntent
    data class ChangeTagAccessFlag(val newValue: INVENTORY_STATE): ReaderSettingIntent
    data class ChangeUsedChannel(val newValue: List<String>): ReaderSettingIntent
    data class ChangeRadioPowerSliderPosition(val newValue: Int): ReaderSettingIntent
}