package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession

sealed interface SettingIntent {
    data class ChangeVolume(val newValue: FakeBeeperVolume): SettingIntent
    data class ChangeRadioPower(val newValue: Int): SettingIntent
    data class ChangeSession(val newValue: FakeSession): SettingIntent
    data class ChangeTagPopulation(val newValue: String): SettingIntent
    data class ChangeTagAccessFlag(val newValue: FakeInventoryState): SettingIntent
    data class ChangeUsedChannel(val newValue: List<FakeChannel>): SettingIntent
    data class ChangeRadioPowerSliderPosition(val newValue: Int): SettingIntent
    data class  ToggleUnsaveConfirmDialog(val showUnsaveConfirmDialog: Boolean): SettingIntent
    data class  ToggleApplySettingConfirmDialog(val showApplySettingConfirmDialog: Boolean): SettingIntent
    data object ResetToInitialSetting: SettingIntent
}