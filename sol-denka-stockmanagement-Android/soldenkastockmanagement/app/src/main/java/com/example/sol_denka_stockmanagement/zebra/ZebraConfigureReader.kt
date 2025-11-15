package com.example.sol_denka_stockmanagement.zebra

import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.zebra.rfid.api3.*

object ZebraConfigureReader {

    fun configureReader(
        reader: RFIDReader,
        eventListener: ((DeviceEvent) -> Unit)?,
        deviceManager: ZebraDeviceManager
    ) {
        if (reader.isConnected) {
            val zebraEventListener = ZebraEventListener(
                reader,
                eventListener = eventListener,
                deviceManager = deviceManager
            )

            deviceManager.setZebraEventListener(zebraEventListener)

            val triggerInfo = TriggerInfo().apply {
                StartTrigger.triggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE
                StopTrigger.triggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE
            }

            try {
                // --- Event settings ---
                reader.Events.addEventsListener(zebraEventListener)
                reader.Events.setHandheldEvent(true)
                reader.Events.setBatteryEvent(true)
                reader.Events.setTagReadEvent(true)
                reader.Events.setAttachTagDataWithReadEvent(false)

                // --- Basic Config ---
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true)
                reader.Config.getDeviceStatus(true, false, false)
                reader.Config.startTrigger = triggerInfo.StartTrigger
                reader.Config.stopTrigger = triggerInfo.StopTrigger

                // --- Clear existing prefilters ---
                reader.Actions.PreFilters.deleteAll()

                // --- Add prefilter ---
                val preFilter = PreFilters().PreFilter().apply {
                    antennaID = 1.toShort()
                }

                // Add the prefilter to reader
                reader.Actions.PreFilters.add(preFilter)


            } catch (e: InvalidUsageException) {
                e.printStackTrace()
            } catch (e: OperationFailureException) {
                e.printStackTrace()
            }
        }
    }
}