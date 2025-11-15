package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.SESSION
import kotlin.math.pow
import kotlin.math.roundToInt

data class ReaderInfoModel(
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val readerName: String = "",
    val batteryLevel: Int = 0,
    val radioPower: Int = 0,
    val radioPowerMw: Int = calculateMwFromDbm(0),
    val buzzerVolume: BEEPER_VOLUME = BEEPER_VOLUME.QUIET_BEEP,
    val firmwareVer: String = "",
    var rfidSession: SESSION = SESSION.SESSION_S0,
    var tagPopulation: Short = 0,
    var tagAccessFlag: INVENTORY_STATE = INVENTORY_STATE.INVENTORY_STATE_AB_FLIP,
    var rfidRadioIrradiation: Int = -1,
    var supportedChannels: List<String> = emptyList(),
){
    companion object {
        fun calculateMwFromDbm(dbm: Int): Int = 10.0.pow(dbm / 10.0).roundToInt()
    }
}
