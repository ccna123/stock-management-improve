package com.tss.sol_bs_tirescan_app.zebra

import android.content.Context
import android.util.Log
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.zebra.scannercontrol.DCSSDKDefs
import com.zebra.scannercontrol.DCSScannerInfo
import com.zebra.scannercontrol.IDcsSdkApiDelegate
import com.zebra.scannercontrol.SDKHandler

class BarcodeScannerInterface(
    private val context: Context,
    private val eventListener: (DeviceEvent) -> Unit
) : IDcsSdkApiDelegate {
    private var sdkHandler: SDKHandler? = null
    private var scannerInfoList: ArrayList<DCSScannerInfo> = ArrayList()

    fun initializeScannerSDK(): Boolean {
        return try {
            if (sdkHandler == null) {
                sdkHandler = SDKHandler(context, false)
                val btResult = sdkHandler?.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_NORMAL)
                if (btResult != DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS) {
                    Log.w("TSS", "Failed to set BT_NORMAL mode, result: $btResult")
                    sdkHandler?.dcssdkSetOperationalMode(DCSSDKDefs.DCSSDK_MODE.DCSSDK_OPMODE_BT_LE)
                }
                Log.d("TSS", "Scanner SDK operational mode set: $btResult")

                sdkHandler?.dcssdkSetDelegate(this)
                val notificationsMask =
                    DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_APPEARANCE.value or
                            DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SCANNER_DISAPPEARANCE.value or
                            DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_ESTABLISHMENT.value or
                            DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_SESSION_TERMINATION.value or
                            DCSSDKDefs.DCSSDK_EVENT.DCSSDK_EVENT_BARCODE.value
                sdkHandler?.dcssdkSubsribeForEvents(notificationsMask)
                Log.d("TSS", "Subscribed to scanner events: $notificationsMask")
                sdkHandler?.dcssdkEnableAvailableScannersDetection(true)
            }
            true
        } catch (e: Exception) {
            Log.e("TSS", "Error initializing scanner SDK: ${e.message}", e)
            false
        }
    }

    fun getAvailableScanners(): ArrayList<DCSScannerInfo> {
        scannerInfoList.clear()
        try {
            sdkHandler?.dcssdkGetAvailableScannersList(scannerInfoList)
            Log.d("TSS", "Found ${scannerInfoList.size} available scanners")
            scannerInfoList.forEach { scanner ->
                Log.d("TSS", "Scanner: ID=${scanner.scannerID}, Name=${scanner.scannerName}, Active=${scanner.isActive}")
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error getting available scanners: ${e.message}", e)
        }
        return scannerInfoList
    }

    fun getActiveScanners(): ArrayList<DCSScannerInfo> {
        val activeScanners = ArrayList<DCSScannerInfo>()
        try {
            sdkHandler?.dcssdkGetActiveScannersList(activeScanners)
            Log.d("TSS", "Found ${activeScanners.size} active scanners")
            activeScanners.forEach { scanner ->
                Log.d("TSS", "Active Scanner: ID=${scanner.scannerID}, Name=${scanner.scannerName}, Active=${scanner.isActive}")
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error getting active scanners: ${e.message}", e)
        }
        return activeScanners
    }

    fun connectToScanner(scannerID: Int): Boolean {
        try {
            val scanner = scannerInfoList.firstOrNull { it.scannerID == scannerID }
                ?: getActiveScanners().firstOrNull { it.scannerID == scannerID }
            if (scanner == null) {
                Log.e("TSS", "No scanner found with ID: $scannerID")
                return false
            }
            if (scanner.isActive) {
                Log.d("TSS", "Scanner ID $scannerID is already active")
                return true
            }

            val result = sdkHandler?.dcssdkEstablishCommunicationSession(scannerID)
            if (result == DCSSDKDefs.DCSSDK_RESULT.DCSSDK_RESULT_SUCCESS) {
                Log.d("TSS", "Connected to scanner ID: $scannerID, Name: ${scanner.scannerName}")
                sdkHandler?.dcssdkEnableAutomaticSessionReestablishment(false, scannerID)
                enableScanning(scannerID)
                configureSymbologies(scannerID)
                return true
            } else {
                Log.e("TSS", "Failed to connect to scanner ID: $scannerID, Result: $result")
                return false
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error connecting to scanner ID: $scannerID, ${e.message}", e)
            return false
        }
    }

    private fun enableScanning(scannerID: Int) {
        try {
            val inXml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
            val outXml = StringBuilder()
            val result = sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_SCAN_ENABLE,
                inXml,
                outXml,
                scannerID
            )
            Log.d("TSS", "Enable scanning result: $result, Output: $outXml")
        } catch (e: Exception) {
            Log.e("TSS", "Error enabling scanning: ${e.message}", e)
        }
    }

    private fun configureSymbologies(scannerID: Int) {
        try {
            val symbologies = listOf(
                1, // Code39
                2, // Code128
                3, // Code93
                8, // UPC-A
                9  // UPC-E
            )
            symbologies.forEach { symbologyId ->
                val inXml = """
                    <inArgs>
                        <scannerID>$scannerID</scannerID>
                        <cmdArgs>
                            <arg-xml>
                                <attrib_list>
                                    <attribute>
                                        <id>$symbologyId</id>
                                        <datatype>F</datatype>
                                        <value>true</value>
                                    </attribute>
                                </attrib_list>
                            </arg-xml>
                        </cmdArgs>
                    </inArgs>
                """.trimIndent()
                val outXml = StringBuilder()
                val result = sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                    DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET,
                    inXml,
                    outXml,
                    scannerID
                )
                Log.d("TSS", "Set symbology ID $symbologyId result: $result, Output: $outXml")
            }
        } catch (e: Exception) {
            Log.e("TSS", "Error configuring symbologies: ${e.message}", e)
        }
    }

    fun testScannerHardware(scannerID: Int) {
        try {
            // Turn on LED pattern
            var inXml = "<inArgs><scannerID>$scannerID</scannerID><cmdArgs><arg-int>88</arg-int></cmdArgs></inArgs>"
            var outXml = StringBuilder()
            sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION,
                inXml,
                outXml,
                scannerID
            )
            Log.d("TSS", "Turn on LED pattern: $outXml")
            Thread.sleep(400)

            // Beep
            inXml = "<inArgs><scannerID>$scannerID</scannerID><cmdArgs><arg-int>2</arg-int></cmdArgs></inArgs>"
            outXml = StringBuilder()
            sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION,
                inXml,
                outXml,
                scannerID
            )
            Log.d("TSS", "Beep scanner: $outXml")
            Thread.sleep(400)

            // Vibrate
            inXml = "<inArgs><scannerID>$scannerID</scannerID><cmdArgs></cmdArgs></inArgs>"
            outXml = StringBuilder()
            sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_VIBRATION_FEEDBACK,
                inXml,
                outXml,
                scannerID
            )
            Log.d("TSS", "Vibrate scanner: $outXml")
            Thread.sleep(400)

            // Turn off LED pattern
            inXml = "<inArgs><scannerID>$scannerID</scannerID><cmdArgs><arg-int>90</arg-int></cmdArgs></inArgs>"
            outXml = StringBuilder()
            sdkHandler?.dcssdkExecuteCommandOpCodeInXMLForScanner(
                DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION,
                inXml,
                outXml,
                scannerID
            )
            Log.d("TSS", "Turn off LED pattern: $outXml")
        } catch (e: Exception) {
            Log.e("TSS", "Error testing scanner hardware: ${e.message}", e)
        }
    }

    fun disconnect(scannerID: Int) {
        try {
            sdkHandler?.dcssdkTerminateCommunicationSession(scannerID)
            Log.d("TSS", "Disconnected from scanner ID: $scannerID")
        } catch (e: Exception) {
            Log.e("TSS", "Error disconnecting scanner: ${e.message}", e)
        }
    }

    fun onDestroy() {
        try {
            sdkHandler?.let {
                scannerInfoList.forEach { scanner ->
                    if (scanner.isActive) {
                        disconnect(scanner.scannerID)
                    }
                }
                sdkHandler = null
            }
            scannerInfoList.clear()
            Log.d("TSS", "BarcodeScannerInterface destroyed")
        } catch (e: Exception) {
            Log.e("TSS", "Error during onDestroy: ${e.message}", e)
        }
    }

    override fun dcssdkEventScannerAppeared(scannerInfo: DCSScannerInfo?) {
        scannerInfo?.let {
            if (!scannerInfoList.contains(it)) {
                scannerInfoList.add(it)
                Log.d("TSS", "Scanner appeared: ${it.scannerName}, ID: ${it.scannerID}")
            }
        }
    }

    override fun dcssdkEventScannerDisappeared(scannerId: Int) {
        scannerInfoList.removeIf { it.scannerID == scannerId }
        Log.d("TSS", "Scanner disappeared: $scannerId")
        eventListener(DeviceEvent.Disconnected)
    }

    override fun dcssdkEventCommunicationSessionEstablished(scannerInfo: DCSScannerInfo?) {
        scannerInfo?.let {
            Log.d("TSS", "Scanner session established: ${it.scannerName}, ID: ${it.scannerID}")
        }
    }

    override fun dcssdkEventCommunicationSessionTerminated(scannerId: Int) {
        Log.d("TSS", "Scanner session terminated: $scannerId")
        eventListener(DeviceEvent.Disconnected)
        eventListener.invoke(DeviceEvent.DeviceLinkLost(true))
    }

    override fun dcssdkEventBarcode(barcode: ByteArray?, barcodeType: Int, scannerID: Int) {
        barcode?.let {
            val barcodeString = String(it)
            Log.d("TSS", "Barcode scanned: $barcodeString, Type: $barcodeType, ScannerID: $scannerID")
            eventListener(DeviceEvent.BarcodeScanned(barcodeString, barcodeType))
        } ?: Log.e("TSS", "Received null barcode data for ScannerID: $scannerID, Type: $barcodeType")
    }

    override fun dcssdkEventImage(image: ByteArray?, scannerId: Int) {
        Log.d("TSS", "Image received from scanner: $scannerId")
    }

    override fun dcssdkEventVideo(video: ByteArray?, scannerId: Int) {
        Log.d("TSS", "Video received from scanner: $scannerId")
    }

    override fun dcssdkEventBinaryData(data: ByteArray?, scannerId: Int) {
        Log.d("TSS", "Binary data received from scanner: $scannerId")
    }

    override fun dcssdkEventFirmwareUpdate(event: com.zebra.scannercontrol.FirmwareUpdateEvent?) {
        Log.d("TSS", "Firmware update event: ${event?.toString()}")
    }

    override fun dcssdkEventAuxScannerAppeared(scannerInfo: DCSScannerInfo?, auxScannerInfo: DCSScannerInfo?) {
        Log.d("TSS", "Aux scanner appeared: ${scannerInfo?.scannerName}, ${auxScannerInfo?.scannerName}")
    }
}