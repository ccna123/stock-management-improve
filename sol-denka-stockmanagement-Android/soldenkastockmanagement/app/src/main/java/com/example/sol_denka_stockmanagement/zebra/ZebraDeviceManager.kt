package com.example.sol_denka_stockmanagement.zebra

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.IDeviceManager
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.model.TagInfoModel
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.zebra.rfid.api3.Antennas
import com.zebra.rfid.api3.BEEPER_VOLUME
import com.zebra.rfid.api3.ENUM_TRANSPORT
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.InvalidUsageException
import com.zebra.rfid.api3.OperationFailureException
import com.zebra.rfid.api3.RFIDReader
import com.zebra.rfid.api3.ReaderDevice
import com.zebra.rfid.api3.Readers
import com.zebra.rfid.api3.RegionInfo
import com.zebra.rfid.api3.RegulatoryConfig
import com.zebra.rfid.api3.SESSION
import com.zebra.scannercontrol.DCSScannerInfo
import com.zebra.scannercontrol.SDKHandler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ZebraDeviceManager(private val context: Context) : IDeviceManager,
    Readers.RFIDReaderEventHandler {

    private var readers: Readers? = null
    private var reader: RFIDReader? = null
    private var eventListener: ((DeviceEvent) -> Unit)? = null
    private var readerList: List<ReaderDevice> = emptyList()
    private var singulationControl: Antennas.SingulationControl? = null
    private var regulatoryConfig: RegulatoryConfig? = null
    private var sdkHandler: SDKHandler? = null
    private var scannerList: List<DCSScannerInfo> = emptyList()
    private var scannerId: Int = 0
    private var zebraEventListener: ZebraEventListener? = null

    private var dataWedgeReceiver: ZebraDataWedgeReceiver? = null
    private var isInternalScanner = false

    private var lastBatteryLevel: Int = 0

    private val scannedTags = mutableSetOf<TagInfoModel>()

    fun initializeIfInternalScanner() {
        Log.d("TSS", "initializeIfInternalScanner is called")
        isInternalScanner =
            Build.MODEL.startsWith("TC21") || Build.MODEL.startsWith("TC22") || Build.MODEL.startsWith(
                "EC"
            )

        if (isInternalScanner) {
            Log.d("TSS", "Detected internal TC22/TC2x scanner — initializing DataWedge immediately")
            registerDataWedgeReceiver()
            DataWedgeProfileConfig.setupProfile(context)
        }
    }

    suspend fun autoConnectIfRFD4030Attached(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (readers == null) {
                    readers = Readers(context, ENUM_TRANSPORT.SERVICE_USB)
                    Readers.attach(this@ZebraDeviceManager)
                }
                val availableReaders = readers?.GetAvailableRFIDReaderList() ?: emptyList()
                if (availableReaders.isEmpty()) {
                    Log.d("TSS", "No attached RFD readers detected")
                    return@withContext false
                }

                val rfdReader = availableReaders.find { it.name.startsWith("RFD") }
                if (rfdReader != null) {
                    reader = rfdReader.rfidReader
                    if (!reader!!.isConnected) {
                        reader!!.connect()
                        ZebraConfigureReader.configureReader(
                            reader!!,
                            eventListener,
                            this@ZebraDeviceManager
                        )

//                        // Initialize singulation control
                        singulationControl = reader?.Config?.Antennas?.getSingulationControl(1)
                        regulatoryConfig = reader?.Config?.regulatoryConfig

//                        // Fetch reader info
                        val supportedRegions =
                            reader!!.ReaderCapabilities.SupportedRegions.getRegionInfo(0)
                        val channels: List<String> = getChannels(supportedRegions)

                        // Notify connection success on main thread
                        withContext(Dispatchers.Main) {
                            eventListener?.invoke(DeviceEvent.ConnectionStateChanged(true))
                            eventListener?.invoke(
                                DeviceEvent.ConnectedWithInfo(
                                    batteryPower = getLastBatteryLevel(),
                                    radioPower = (reader!!.ReaderCapabilities.transmitPowerLevelValues.size - 1) / 10,
                                    session = singulationControl?.session ?: SESSION.SESSION_S0,
                                    channel = regulatoryConfig!!.enabledchannels.toList(),
                                    supportedChannels = channels,
                                    tagPopulation = singulationControl?.tagPopulation ?: 0,
                                    buzzerVolume = reader!!.Config.beeperVolume,
                                    tagAccessFlag = singulationControl?.Action?.inventoryState
                                        ?: INVENTORY_STATE.INVENTORY_STATE_AB_FLIP,
                                    firmwareVersion = reader!!.ReaderCapabilities.firwareVersion,
                                    readerName = reader!!.hostName,
                                    connectionState = ConnectionState.CONNECTED
                                )
                            )
                        }
                        return@withContext true
                    }
                } else {
                    Log.d("TSS", "No RFD reader found in attached list")
                }
                false
            } catch (e: OperationFailureException) {
                Log.e(
                    "TSS",
                    "OperationFailureException: ${e.message}, Vendor: ${e.vendorMessage}, Results: ${e.results}",
                    e
                )
                withContext(Dispatchers.Main) {
                    eventListener?.invoke(DeviceEvent.Disconnected)
                }
                false
            } catch (e: Exception) {
                Log.e("TSS", "autoConnectIfRFD4030Attached failed: ${e.message}")
                false
            }
        }
    }

    fun setZebraEventListener(listener: ZebraEventListener) {
        this.zebraEventListener = listener
    }


//    fun toggleDataWedgeReceiver(enable: Boolean) {
//        if (enable) {
//            registerDataWedgeReceiver()
//        } else {
//            unregisterDataWedgeReceiver()
//        }
//    }

    // --- New helper: register DataWedge ---
    private fun registerDataWedgeReceiver() {
        if (dataWedgeReceiver != null) return

        // Create an instance of ZebraDataWedgeReceiver
        dataWedgeReceiver = ZebraDataWedgeReceiver { barcode, labelType, source ->
            Log.d(
                "TSS",
                "registerDataWedgeReceiver: Barcode=$barcode, LabelType=$labelType, Source=$source"
            )
            if (eventListener == null) {
                Log.e("TSS", "registerDataWedgeReceiver: eventListener is null")
            } else {
                scannedTags.clear()
                scannedTags.add(
                    TagInfoModel(
                        rfidNo = barcode,
                    )
                )
                eventListener?.invoke(DeviceEvent.TagsScanned(scannedTags.toList()))
            }
        }

        val filter = IntentFilter().apply {
            addAction("com.example.sol_denka_stockmanagement.datawedge_action")
            addCategory("com.example.sol_denka_stockmanagement.datawedge_category")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                dataWedgeReceiver,
                filter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            context.registerReceiver(
                dataWedgeReceiver,
                filter,
                Context.RECEIVER_EXPORTED
            )
        }

        Log.i("TSS", "DataWedge receiver registered for internal scanner")
    }

    private fun unregisterDataWedgeReceiver() {
        try {
            dataWedgeReceiver?.let {
                context.unregisterReceiver(it)
                Log.d("TSS", "DataWedge receiver unregistered")
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error unregistering DataWedge receiver: ${e.message}")
        } finally {
            dataWedgeReceiver = null
        }
    }

    private fun getChannels(region: RegionInfo): List<String> {
        return try {
            reader?.let { rfidReader ->
                if (!rfidReader.isConnected) {
                    Log.e("TSS", "Cannot get channels: Reader is not connected")
                    return emptyList()
                }
                val regionInfo = rfidReader.Config.getRegionInfo(region)
                val channels = regionInfo.supportedChannels?.map { it.toString() } ?: emptyList()
                channels
            } ?: run {
                Log.e("TSS", "Cannot get channels: Reader is null")
                emptyList()
            }
        } catch (e: InvalidUsageException) {
            Log.e("TSS", "InvalidUsageException in getChannels: ${e.message}, Info: ${e.info}", e)
            emptyList()
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "OperationFailureException in getChannels: ${e.message}, Vendor: ${e.vendorMessage}, Results: ${e.results}",
                e
            )
            emptyList()
        } catch (e: Exception) {
            Log.e("TSS", "Unexpected error in getChannels: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun disconnect() {
        try {
            reader?.let {
                it.disconnect()
                Log.d("TSS", "Disconnected from RFID reader")
            }
            if (sdkHandler != null && scannerId != 0) {
                sdkHandler?.dcssdkTerminateCommunicationSession(scannerId)
                scannerId = 0
                scannerList = emptyList()
                Log.d("TSS", "Disconnected from scanner")
            }
            withContext(Dispatchers.Main) {
                eventListener?.invoke(DeviceEvent.Disconnected)
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error disconnecting: ${e.message}", e)
        }
    }

    override fun cleanup() {
        try {
            unregisterDataWedgeReceiver()
            readers?.Dispose()
            Readers.deattach(this)
            if (sdkHandler != null && scannerId != 0) {
                sdkHandler?.dcssdkTerminateCommunicationSession(scannerId)
                scannerId = 0
                scannerList = emptyList()
            }
            sdkHandler = null
            readers = null
            reader = null
            readerList = emptyList()
            singulationControl = null
            Log.d("TSS", "Cleaned up ZebraDeviceManager")
        } catch (e: Exception) {
            Log.e("TSS", "Error during cleanup: ${e.message}", e)
        }
    }

    override fun setEventListener(listener: (DeviceEvent) -> Unit) {
        eventListener = listener
    }

    override fun setSession(session: SESSION): Boolean {
        return try {
            singulationControl = reader?.Config?.Antennas?.getSingulationControl(1)
            singulationControl?.session = session
            reader?.Config?.Antennas?.setSingulationControl(1, singulationControl)
            true
        } catch (e: InvalidUsageException) {
            Log.e("TSS", e.vendorMessage)
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRfidSession) error"))
            false
        } catch (e: OperationFailureException) {
            Log.e("TSS", e.statusDescription)
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRfidSession) error"))
            false
        } catch (e: Exception) {
            Log.e("TSS", "Error setting RFID session: ${e.message}", e)
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRfidSession) error"))
            false
        }
    }

    override fun setTagAccessFlag(flag: INVENTORY_STATE): Boolean {
        return try {
            singulationControl = reader?.Config?.Antennas?.getSingulationControl(1)
            singulationControl?.Action?.inventoryState = flag
            reader?.Config?.Antennas?.setSingulationControl(1, singulationControl)
            true
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (setTagSelectionFlag) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setTagSelectionFlag) error"))
            false
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (setTagSelectionFlag) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setTagSelectionFlag) error"))
            false
        } catch (e: Exception) {
            Log.e("TSS", "RFD_40 (setTagSelectionFlag) error: ${e.message}")
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setTagSelectionFlag) error"))
            false
        }
    }

    override fun setChannel(channel: List<String>): Boolean {
        return try {
            val regulatoryConfig = reader!!.Config.regulatoryConfig
            regulatoryConfig.setEnabledChannels(channel.toTypedArray())
            reader!!.Config.regulatoryConfig = regulatoryConfig
            true
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (setChannel) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setChannel) error"))
            false
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (setChannel) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setChannel) error"))
            false
        } catch (e: Exception) {
            Log.e("TSS", "RFD_40 (setChannel) error: ${e.message}")
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setChannel) error"))
            false
        }
    }

    override fun setRadioPower(radioPower: Int): Boolean {
        return try {
            reader?.Config?.Antennas?.Config()?.transmitPowerIndex = radioPower.toShort()
            true
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (setRadioPower) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRadioPower) error"))
            false
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (setRadioPower) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRadioPower) error"))
            false
        } catch (e: Exception) {
            Log.e("TSS", "RFD_40 (setRadioPower) error: ${e.message}")
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setRadioPower) error"))
            false
        }
    }

    override fun setBuzzerVolume(buzzerVolume: BEEPER_VOLUME): Boolean {
        return try {
            reader?.Config?.beeperVolume = buzzerVolume
            true
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (setVolume) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setVolume) error"))
            false
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (setVolume) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setVolume) error"))
            false
        } catch (e: Exception) {
            Log.e("TSS", "RFD_40 (setVolume) error: ${e.message}")
            eventListener?.invoke(DeviceEvent.GeneralEventError("RFD_40 (setVolume) error"))
            false
        }
    }

    override suspend fun startInventory() {
        try {
            reader?.Actions?.Inventory?.perform()
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (startInventory) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (startInventory) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
        }
    }

    override suspend fun stopInventory() {
        try {
            reader?.Actions?.Inventory?.stop()
        } catch (e: InvalidUsageException) {
            Log.e(
                "TSS",
                "RFD_40 (stopInventory) error: Info: ${e.info}, Vendor: ${e.vendorMessage}"
            )
        } catch (e: OperationFailureException) {
            Log.e(
                "TSS",
                "RFD_40 (stopInventory) error: Result: ${e.results}, Vendor: ${e.vendorMessage}, Status: ${e.statusDescription}"
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun RFIDReaderAppeared(reader: ReaderDevice?) {
        Log.w("TSS", "RFIDReaderAppeared: $reader appear")
        GlobalScope.launch(Dispatchers.IO) {
            autoConnectIfRFD4030Attached()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun RFIDReaderDisappeared(reader: ReaderDevice?) {
        Log.w("TSS", "RFIDReaderDisappeared: $reader disappear")
        GlobalScope.launch(Dispatchers.IO) {
            eventListener?.invoke(DeviceEvent.Disconnected)
        }
    }

    fun getLastBatteryLevel(): Int = lastBatteryLevel
    fun updateLastBatteryLevel(level: Int) {
        lastBatteryLevel = level
    }
}