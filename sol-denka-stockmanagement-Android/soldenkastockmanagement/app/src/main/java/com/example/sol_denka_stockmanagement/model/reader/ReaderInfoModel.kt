package com.example.sol_denka_stockmanagement.model.reader

import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import kotlin.math.pow
import kotlin.math.roundToInt

data class ReaderInfoModel(
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val readerName: String = "",
    val batteryLevel: Int = 0,
    val radioPower: Int = 0,
    val radioPowerMw: Int = calculateMwFromDbm(0),
    val buzzerVolume: FakeBeeperVolume = FakeBeeperVolume.QUIET_BEEP,
    val firmwareVer: String = "",
    var rfidSession: FakeSession = FakeSession.SESSION_S0,
    var tagPopulation: Short = 0,
    var tagAccessFlag: FakeInventoryState = FakeInventoryState.INVENTORY_STATE_AB_FLIP,
    var rfidRadioIrradiation: Int = -1,
    var supportedChannels: List<FakeChannel> = emptyList(),
){
    companion object {
        fun calculateMwFromDbm(dbm: Int): Int = 10.0.pow(dbm / 10.0).roundToInt()
    }
}