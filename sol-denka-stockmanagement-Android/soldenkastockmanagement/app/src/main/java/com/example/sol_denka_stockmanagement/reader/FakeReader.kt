package com.example.sol_denka_stockmanagement.reader

import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.model.common.TagInfoModel
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

enum class FakeSession { SESSION_S0, SESSION_S1, SESSION_S2, SESSION_S3 }
enum class FakeInventoryState { INVENTORY_STATE_AB_FLIP, INVENTORY_STATE_A, INVENTORY_STATE_B }
enum class FakeBeeperVolume(val displayName: String) {
    QUIET_BEEP("静音"), LOW_BEEP("小"), MEDIUM_BEEP("中"),
    HIGH_BEEP("高"),
}

enum class FakeChannel { CHANNEL1, CHANNEL2, CHANNEL3, CHANNEL4, CHANNEL5, CHANNEL6 }

class FakeReader {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val _batteryLevel = MutableStateFlow(100)
    val batteryLevel: StateFlow<Int> get() = _batteryLevel

    private val _tagFlow = MutableStateFlow<Pair<String, Float>?>(null)
    val tagFlow: StateFlow<Pair<String, Float>?> get() = _tagFlow

    private var eventListener: ((DeviceEvent) -> Unit)? = null

    private var inventoryJob: Job? = null
    private var batteryJob: Job? = null

    // -----------------------------
    // Fake Reader Settings Storage
    // -----------------------------
    private var currentSession: FakeSession = FakeSession.SESSION_S0
    private var currentInventoryState: FakeInventoryState = FakeInventoryState.INVENTORY_STATE_A
    private var currentBeeper: FakeBeeperVolume = FakeBeeperVolume.MEDIUM_BEEP
    private var currentRadioPower: Int = 30
    private var currentTagPopulation: Short = 30
    private var currentChannels: List<FakeChannel> = FakeChannel.entries.toList()

    private var idleDrainJob: Job? = null
    private var scanDrainJob: Job? = null

    fun setEventListener(listener: (DeviceEvent) -> Unit) {
        this.eventListener = listener
    }

    private fun startIdleDrain() {
        // Stop scan drain if running
        scanDrainJob?.cancel()

        idleDrainJob?.cancel()
        idleDrainJob = scope.launch {
            while (isActive) {
                delay(2000) // 2s interval for idle mode
                drainBattery(1)  // drain 1% every 2 seconds
            }
        }
    }

    private fun startScanDrain() {
        // Stop idle drain when scanning
        idleDrainJob?.cancel()

        scanDrainJob?.cancel()
        scanDrainJob = scope.launch {
            while (isActive) {
                delay(800) // faster drain while scanning
                drainBattery(3) // drain 3% every ~1s
            }
        }
    }

    private fun stopAllDrain() {
        idleDrainJob?.cancel()
        scanDrainJob?.cancel()
    }

    // -----------------------------
    // Battery Drain Timer
    // -----------------------------
    // Connection
    // -----------------------------
    suspend fun connect(): Boolean {
        delay(900)
        _isConnected.value = true

//        startIdleDrain()

//        startBatteryDrain()

        eventListener?.invoke(DeviceEvent.ConnectionStateChanged(true))

        eventListener?.invoke(
            DeviceEvent.ConnectedWithInfo(
                batteryPower = _batteryLevel.value,
                radioPower = currentRadioPower,
                session = currentSession,
                channel = currentChannels,
                supportedChannels = FakeChannel.entries.toList(),
                tagPopulation = currentTagPopulation,
                buzzerVolume = currentBeeper,
                tagAccessFlag = currentInventoryState,
                firmwareVersion = "FAKE-1.0.0",
                readerName = "FAKE-READER-01",
                connectionState = ConnectionState.CONNECTED
            )
        )

        return true
    }

    suspend fun disconnect() {
        inventoryJob?.cancel()
        batteryJob?.cancel()

        delay(300)
        _isConnected.value = false

        eventListener?.invoke(DeviceEvent.ConnectionStateChanged(false))
        eventListener?.invoke(DeviceEvent.Disconnected)
    }

    // -----------------------------
    // Inventory
    // -----------------------------
    fun startInventory() {
        inventoryJob?.cancel()
        inventoryJob = scope.launch {
            startScanDrain()
            while (isActive) {
                delay(250)
                val epc = listOf(
                    "80000000",
                    "81111111",
                    "82222222",
                    "83333333",
                    "84444444",
                    "85555555",
                    "86666666",
                    "87777777",
                    "88888888",
                    "89999999",
                    "90000000",
                    "91111111",
                    "92222222",
                    "93333333",
                    "94444444",
                    "95555555",
                    "96666666",
                    "97777777",
                    "98888888",
                    "99999999",
                ).random()

                val rssi = Random.nextInt(-65, -20).toFloat()

                val tag = TagInfoModel(rfidNo = epc, rssi = rssi)

                eventListener?.invoke(DeviceEvent.TagsScanned(listOf(tag)))
            }
        }
    }

    private fun drainBattery(amount: Int) {
        val newLevel = (_batteryLevel.value - amount).coerceAtLeast(5)
        _batteryLevel.value = newLevel
        eventListener?.invoke(DeviceEvent.BatteryData(newLevel))
    }

    fun stopInventory() {
        inventoryJob?.cancel()
        scanDrainJob?.cancel()
        startIdleDrain()
        eventListener?.invoke(DeviceEvent.IsTriggerReleased(true))
    }

    fun emitUpdatedInfo() {
        eventListener?.invoke(
            DeviceEvent.ConnectedWithInfo(
                batteryPower = _batteryLevel.value,
                radioPower = currentRadioPower,
                session = currentSession,
                channel = currentChannels,
                supportedChannels = FakeChannel.entries.toList(),
                tagPopulation = currentTagPopulation,
                buzzerVolume = currentBeeper,
                tagAccessFlag = currentInventoryState,
                firmwareVersion = "FAKE-1.0.0",
                readerName = "FAKE-READER-01",
                connectionState = ConnectionState.CONNECTED
            )
        )
    }


    // -----------------------------
    // Fake Setting APIs
    // -----------------------------

    fun setSession(newSession: FakeSession): Boolean {
        currentSession = newSession
        return true
    }

    fun setTagPopulation(newPopulation: Short): Boolean {
        currentTagPopulation = newPopulation
        return true
    }

    fun setTagAccessFlag(flag: FakeInventoryState): Boolean {
        currentInventoryState = flag
        return true
    }

    fun setBuzzerVolume(vol: FakeBeeperVolume): Boolean {
        currentBeeper = vol
        return true
    }

    fun setRadioPower(power: Int): Boolean {
        currentRadioPower = power
        return true
    }

    fun setChannel(channels: List<FakeChannel>): Boolean {
        currentChannels = channels
        return true
    }
}
