package com.example.sol_denka_stockmanagement.share

import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.model.TagInfoModel
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession

sealed class DeviceEvent {
    data class ConnectionStateChanged(val isConnected: Boolean) : DeviceEvent()
    data object Disconnected : DeviceEvent()

    // New event to carry all device info on connection
    data class ConnectedWithInfo(
        val connectionState: ConnectionState,
        val readerName: String,
        val batteryPower: Int,
        val radioPower: Int,
        val tagPopulation: Short,
        val supportedChannels: List<FakeChannel>? = emptyList(),
        val session: FakeSession,
        val channel: List<FakeChannel>,
        val buzzerVolume: FakeBeeperVolume,
        val tagAccessFlag: FakeInventoryState,
        val firmwareVersion: String,
    ) : DeviceEvent()

    data class TagsScanned(val epcs: List<TagInfoModel>) : DeviceEvent()
    data class BarcodeScanned(val barCode: String, val barCodeType: Int? = null) : DeviceEvent()
    data class DeviceFound(val name: String, val address: String) : DeviceEvent()
    data class GeneralEventError(val error: String) : DeviceEvent()
    data class DeviceLinkLost(val isLinkLost: Boolean) : DeviceEvent()
    data class IsTriggerReleased(val isReleased: Boolean) : DeviceEvent()
    data object TriggerPressed: DeviceEvent()
    data class BatteryData(val batteryLevel: Int) : DeviceEvent()
}