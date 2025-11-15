package com.example.sol_denka_stockmanagement.app_interface

import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import com.example.sol_denka_stockmanagement.share.DeviceEvent

interface IDeviceManager {
    suspend fun disconnect()
    fun cleanup()
    fun setEventListener(listener: (DeviceEvent) -> Unit)
    fun setSession(session: FakeSession): Boolean
    fun setTagAccessFlag(flag: FakeInventoryState): Boolean
    fun setChannel(channel: List<FakeChannel>): Boolean
    fun setRadioPower(radioPower: Int): Boolean
    fun setBuzzerVolume(buzzerVolume: FakeBeeperVolume): Boolean
    fun setTagPopulation(population: Short): Boolean

    suspend fun startInventory()
    suspend fun stopInventory()
}