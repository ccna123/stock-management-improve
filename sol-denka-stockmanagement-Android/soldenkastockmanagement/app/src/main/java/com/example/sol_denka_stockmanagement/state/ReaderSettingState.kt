package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import kotlin.math.pow
import kotlin.math.roundToInt

data class ReaderSettingState(
    // Device-specific fields
    val radioPower: Int = 0, // dbm
    val radioPowerMw: Int = calculateMwFromDbm(0), // mW
    val buzzerVolume: FakeBeeperVolume = FakeBeeperVolume.QUIET_BEEP,
    val firmwareVer: String = "",

    // Settings-specific fields (merged from SettingState)
    var radioPowerSliderPosition: Int = 0,
    var rfidSession: FakeSession = FakeSession.SESSION_S0,
    var tagPopulation: Short = 0,
    var linkProfile: Int = 1,
    var tagAccessFlag: FakeInventoryState = FakeInventoryState.INVENTORY_STATE_AB_FLIP,
    var enabledRfidChannel: List<FakeChannel> = emptyList(),
    var supportedChannels: List<FakeChannel> = emptyList()
) {
    companion object {
        fun calculateMwFromDbm(dbm: Int): Int = 10.0.pow(dbm / 10.0).roundToInt()
    }
}