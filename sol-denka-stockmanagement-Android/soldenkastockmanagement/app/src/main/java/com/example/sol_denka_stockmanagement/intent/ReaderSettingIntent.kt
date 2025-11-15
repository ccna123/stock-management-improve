package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession

sealed interface ReaderSettingIntent {
    data class ChangeVolume(val newValue: FakeBeeperVolume): ReaderSettingIntent
    data class ChangeRadioPower(val newValue: Int): ReaderSettingIntent
    data class ChangeSession(val newValue: FakeSession): ReaderSettingIntent
    data class ChangeTagPopulation(val newValue: Short): ReaderSettingIntent
    data class ChangeTagAccessFlag(val newValue: FakeInventoryState): ReaderSettingIntent
    data class ChangeUsedChannel(val newValue: List<FakeChannel>): ReaderSettingIntent
    data class ChangeRadioPowerSliderPosition(val newValue: Int): ReaderSettingIntent
}