package com.example.sol_denka_stockmanagement.helper

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.example.sol_denka_stockmanagement.app_interface.IDeviceManager
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.model.ReaderInfoModel
import com.example.sol_denka_stockmanagement.model.TagInfoModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.example.sol_denka_stockmanagement.state.ReaderSettingState
import com.example.sol_denka_stockmanagement.zebra.ZebraDeviceManager
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.SESSION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
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
    private var zebraManager: ZebraDeviceManager? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _scannedTags = MutableStateFlow<Map<String, TagInfoModel>>(emptyMap())
    val scannedTags: StateFlow<Map<String, TagInfoModel>> = _scannedTags.asStateFlow()

    private val _scannedTags1 = MutableStateFlow<Map<String, Float>>(emptyMap())
    val scannedTags1: StateFlow<Map<String, Float>> = _scannedTags1.asStateFlow()

    private val _scannedTags2 = MutableStateFlow<String>("")
    val scannedTags2: StateFlow<String> = _scannedTags2.asStateFlow()

    private val _scannedTags3 = MutableStateFlow<Set<String>>(emptySet())
    val scannedTags3: StateFlow<Set<String>> = _scannedTags3.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    // one-shot transition events
    private val _connectionEvents = kotlinx.coroutines.flow.MutableSharedFlow<ConnectionState>(
        replay = 0, extraBufferCapacity = 1
    )
    val connectionEvents = _connectionEvents // expose as SharedFlow
    private var lastConnection: ConnectionState = ConnectionState.DISCONNECTED

    private val _isPerformingInventory = MutableStateFlow(false)
    val isPerformingInventory: StateFlow<Boolean> = _isPerformingInventory

    private val _readerInfo = MutableStateFlow(ReaderInfoModel())
    val readerInfo: StateFlow<ReaderInfoModel> = _readerInfo.asStateFlow()

    private val _canScan = mutableStateOf(false)
    private val _screen = mutableStateOf<Screen>(Screen.ReceivingScan)

    init {
        zebraManager = ZebraDeviceManager(context)
        zebraManager?.initializeIfInternalScanner() ?: Log.e("TSS", "zebraManager is null")
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
                zebraManager?.autoConnectIfRFD4030Attached()
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

        zebraManager?.setEventListener(listener)
    }

    fun startInventory(
        isScanScreen: Boolean = false,
    ) {
        _isPerformingInventory.update { true }
        scope.launch {
            zebraManager?.startInventory()
        }
    }

    fun stopInventory(
        isScanScreen: Boolean = false,
    ) {
        _isPerformingInventory.update { false }
        scope.launch {
            resetRssi()
            zebraManager?.stopInventory()
        }
    }

    fun clearScannedTag() {
        _scannedTags.value = emptyMap()
        _scannedTags1.value = emptyMap()
        _scannedTags2.value = ""
        _scannedTags3.value = emptySet()
    }

    fun resetRssi() {
        scope.launch(Dispatchers.IO) {
            try {
                _scannedTags1.update { it.mapValues { -100f } }
            } catch (e: Exception) {
                Log.e("TSS", "resetRssi failed: ${e.message}")
            }
        }
    }


    fun processScannedTags(epcs: List<TagInfoModel>) {
        scope.launch(Dispatchers.Main.immediate) {
            val uniqueTags = epcs.distinctBy { it.rfidNo }
            uniqueTags.forEach { epc ->
                when (_screen.value.routeId) {
                    Screen.SearchTagsScreen("").routeId -> {
                        _scannedTags1.value = _scannedTags1.value + (epc.rfidNo to epc.rssi)
                    }
                    Screen.ReceivingScan.routeId -> {
                        _scannedTags2.value = epc.rfidNo
                    }
                    Screen.ShippingScan.routeId -> {
                        _scannedTags3.update { it + epc.rfidNo }
                    }
                    else -> {
                        _scannedTags.value = _scannedTags.value + (epc.rfidNo to epc)
                    }
                }
            }
        }
    }

    override suspend fun disconnect() {
        zebraManager?.disconnect()
    }

    override fun cleanup() {
        zebraManager?.cleanup()
    }


    override fun setEventListener(listener: (DeviceEvent) -> Unit) {
        zebraManager?.setEventListener(listener)
    }

    override fun setSession(session: SESSION): Boolean {
        return zebraManager?.setSession(session) == true
    }

    override fun setTagAccessFlag(flag: INVENTORY_STATE): Boolean {
        return zebraManager?.setTagAccessFlag(flag) == true
    }

    override fun setChannel(channel: List<String>): Boolean {
        return zebraManager?.setChannel(channel) == true
    }

    override fun setRadioPower(radioPower: Int): Boolean {
        return zebraManager?.setRadioPower(radioPower) == true
    }

    override fun setBuzzerVolume(buzzerVolume: BEEPER_VOLUME): Boolean {
        return zebraManager?.setBuzzerVolume(buzzerVolume) == true
    }

    fun setRfidChannel(newChannelList: List<String>): Boolean {
        return zebraManager?.setChannel(newChannelList) == true
    }

    override suspend fun startInventory() {
        zebraManager?.startInventory()
    }

    override suspend fun stopInventory() {
        zebraManager?.stopInventory()
    }

    fun setScanEnabled(enabled: Boolean, screen: Screen = Screen.ReceivingScan) {
        _canScan.value = enabled
        _screen.value = screen
    }
}