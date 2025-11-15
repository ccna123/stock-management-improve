package com.example.sol_denka_stockmanagement.app_interface

import com.example.sol_denka_stockmanagement.constant.BeeperVolume
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.SESSION

interface IDeviceManager {
    suspend fun disconnect()
    fun cleanup()
    fun setEventListener(listener: (DeviceEvent) -> Unit)
    fun setSession(session: SESSION): Boolean
    fun setTagAccessFlag(flag: INVENTORY_STATE): Boolean
    fun setChannel(channel: List<String>): Boolean
    fun setRadioPower(radioPower: Int): Boolean
    fun setBuzzerVolume(buzzerVolume: BEEPER_VOLUME): Boolean

    suspend fun startInventory()
    suspend fun stopInventory()
}