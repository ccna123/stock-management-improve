package com.example.sol_denka_stockmanagement.helper

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.example.sol_denka_stockmanagement.app_interface.IDeviceManager
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.model.common.TagInfoModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeReader
import com.example.sol_denka_stockmanagement.reader.FakeSession
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.example.sol_denka_stockmanagement.state.ReaderSettingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(DelicateCoroutinesApi::class)
@Singleton
class ReaderController @Inject constructor(
    private val context: Application,
) : IDeviceManager {
    private var fakeReader: FakeReader? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _scannedTags = MutableStateFlow<Map<String, TagInfoModel>>(emptyMap())
    val scannedTags: StateFlow<Map<String, TagInfoModel>> = _scannedTags.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    // one-shot transition events
    private val _connectionEvents = MutableSharedFlow<ConnectionState>(
        replay = 0, extraBufferCapacity = 1
    )
    val connectionEvents = _connectionEvents // expose as SharedFlow
    private var lastConnection: ConnectionState = ConnectionState.DISCONNECTED

    private val _isPerformingInventory = MutableStateFlow(false)
    val isPerformingInventory: StateFlow<Boolean> = _isPerformingInventory

    private val _readerInfo = MutableStateFlow(ReaderInfoModel())
    val readerInfo: StateFlow<ReaderInfoModel> = _readerInfo.asStateFlow()

    private val _canScan = mutableStateOf(false)
    private val _screen = mutableStateOf<Screen>(Screen.Inbound)

    init {
        fakeReader = FakeReader()
        setEventListeners()

        scope.launch {
            delay(3000)
            tryAutoConnect()
        }
//        loadReaderFromSharedPreferences()
    }

    suspend fun tryAutoConnect() {
        repeat(3) { attempt ->
            try {
                fakeReader?.connect()
                return // success
            } catch (e: Exception) {
                Log.e("TSS", "AutoConnect attempt ${attempt + 1} failed: ${e.message}")
                delay((attempt + 1) * 500L) // 0.5s, 1s, 1.5s delay
            }
        }
        Log.e("TSS", "AutoConnect failed after retries")
    }

    private fun setEventListeners() {
        val listener: (DeviceEvent) -> Unit = { event ->
            when (event) {
                is DeviceEvent.ConnectionStateChanged -> {
                    val newState =
                        if (event.isConnected) ConnectionState.CONNECTED else ConnectionState.DISCONNECTED
                    _connectionState.value = newState
                    if (newState != lastConnection) {
                        _connectionEvents.tryEmit(newState)
                        lastConnection = newState
                    }
                }

                is DeviceEvent.Disconnected,
                is DeviceEvent.DeviceLinkLost -> {
                    val newState = ConnectionState.DISCONNECTED
                    _connectionState.value = newState
                    if (newState != lastConnection) {
                        _connectionEvents.tryEmit(newState)
                        lastConnection = newState
                    }
                }

                is DeviceEvent.ConnectedWithInfo -> {
                    _readerInfo.value = ReaderInfoModel(
                        connectionState = event.connectionState,
                        batteryLevel = event.batteryPower,
                        radioPower = event.radioPower,
                        radioPowerMw = ReaderSettingState.calculateMwFromDbm(event.radioPower),
                        tagPopulation = event.tagPopulation,
                        rfidSession = event.session,
                        supportedChannels = event.supportedChannels ?: emptyList(),
                        buzzerVolume = event.buzzerVolume,
                        tagAccessFlag = event.tagAccessFlag,
                        firmwareVer = event.firmwareVersion,
                        readerName = event.readerName
                    )
                }

                is DeviceEvent.TagsScanned -> {
                    if (_canScan.value) {
                        processScannedTags(event.epcs)
                    }
                }

                is DeviceEvent.BarcodeScanned -> {

                }

                is DeviceEvent.DeviceFound -> {
                    Log.d("TSS", "Device found event: ${event.name} (${event.address})")
                }

                is DeviceEvent.GeneralEventError -> {
                }

                is DeviceEvent.IsTriggerReleased -> {
                    _isPerformingInventory.update { false }
                    resetRssi()
                    Log.w("TSS", "Trigger released")
                }

                DeviceEvent.TriggerPressed -> {
                    if (_canScan.value) {
                        _isPerformingInventory.update { true }
                    } else {
                        _isPerformingInventory.update { false }
                    }
                }

                is DeviceEvent.BatteryData -> _readerInfo.update {
                    it.copy(batteryLevel = event.batteryLevel)
                }
            }
        }

        fakeReader?.setEventListener(listener)
    }

    fun clearScannedTag() {
        _scannedTags.value = emptyMap()
    }

    fun emitUpdatedInfoFromFake() {
        fakeReader?.emitUpdatedInfo()
    }


    fun resetRssi() {
        scope.launch(Dispatchers.IO) {
            try {
                _scannedTags.update { old ->
                    old.mapValues { (_, value) ->
                        value.copy(rssi = -100f)
                    }
                }
            } catch (e: Exception) {
                Log.e("TSS", "resetRssi failed: ${e.message}")
            }
        }
    }


    fun processScannedTags(epcs: List<TagInfoModel>) {
        scope.launch(Dispatchers.Main.immediate) {
            val uniqueTags = epcs.distinctBy { it.rfidNo }
            _scannedTags.update { currentTags ->
                val updated = currentTags.toMutableMap()
                uniqueTags.forEach { epc ->
                    updated[epc.rfidNo] = TagInfoModel(
                        rfidNo = epc.rfidNo,
                        rssi = epc.rssi
                    )
                }
                updated
            }
        }
    }

    override suspend fun disconnect() {
        fakeReader?.disconnect()
    }

    override fun cleanup() {
        fakeReader = null
    }


    override fun setEventListener(listener: (DeviceEvent) -> Unit) {
        fakeReader?.setEventListener(listener)
    }

    override fun setSession(session: FakeSession): Boolean {
        return fakeReader?.setSession(session) == true
    }

    override fun setTagAccessFlag(flag: FakeInventoryState): Boolean {
        return fakeReader?.setTagAccessFlag(flag) == true
    }

    override fun setChannel(channel: List<FakeChannel>): Boolean {
        return fakeReader?.setChannel(channel) == true
    }

    override fun setRadioPower(radioPower: Int): Boolean {
        return fakeReader?.setRadioPower(radioPower) == true
    }

    override fun setBuzzerVolume(buzzerVolume: FakeBeeperVolume): Boolean {
        return fakeReader?.setBuzzerVolume(buzzerVolume) == true
    }

    override fun setTagPopulation(population: Short): Boolean {
        return fakeReader?.setTagPopulation(population) == true
    }

    override suspend fun startInventory() {
        _isPerformingInventory.update { true }
        scope.launch {
            fakeReader?.startInventory()
        }
    }

    override suspend fun stopInventory() {
        _isPerformingInventory.update { false }
        scope.launch {
            resetRssi()
            fakeReader?.stopInventory()
        }
    }

    fun setScanEnabled(enabled: Boolean, screen: Screen = Screen.Inbound) {
        _canScan.value = enabled
        _screen.value = screen
    }
}