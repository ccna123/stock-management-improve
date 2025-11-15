package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.BeeperVolume
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.SESSION
import kotlin.math.pow
import kotlin.math.roundToInt

data class ReaderSettingState(
    // Device-specific fields
    val radioPower: Int = 0, // dbm
    val radioPowerMw: Int = calculateMwFromDbm(0), // mW
    val buzzerVolume: BeeperVolume = BeeperVolume.QUIET_BEEP,
    val firmwareVer: String = "",

    // Settings-specific fields (merged from SettingState)
    var radioPowerSliderPosition: Int = 0,
    var rfidSession: SESSION = SESSION.SESSION_S0,
    var tagPopulation: Short = 0,
    var linkProfile: Int = 1,
    var tagAccessFlag: INVENTORY_STATE = INVENTORY_STATE.INVENTORY_STATE_AB_FLIP,
    var enabledRfidChannel: List<String> = emptyList(),
    var supportedChannels: List<String> = emptyList()
) {
    companion object {
        fun calculateMwFromDbm(dbm: Int): Int = 10.0.pow(dbm / 10.0).roundToInt()
    }
}