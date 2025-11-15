package com.example.sol_denka_stockmanagement.zebra

import android.util.Log
import com.example.sol_denka_stockmanagement.model.TagInfoModel
import com.example.sol_denka_stockmanagement.share.DeviceEvent
import com.zebra.rfid.api3.*


class ZebraEventListener(
    private val reader: RFIDReader,
    private val deviceManager: ZebraDeviceManager,
    var eventListener: ((DeviceEvent) -> Unit)?
) : RfidEventsListener {

    private val scannedTags = mutableSetOf<TagInfoModel>()

    override fun eventReadNotify(readEvent: RfidReadEvents?) {
        scannedTags.clear()
        val myTags: Array<TagData> = reader.Actions.getReadTags(100)
        for (index in myTags.indices) {
            Log.e("TSS", "tagId: ${myTags[index].tagID}")
            scannedTags.add(
                TagInfoModel(
                    rfidNo = myTags[index].tagID,
                    rssi = myTags[index].peakRSSI.toFloat(),
                )
            )
        }
        eventListener?.invoke(DeviceEvent.TagsScanned(scannedTags.toList()))
    }

    override fun eventStatusNotify(statusEvent: RfidStatusEvents?) {
        when (statusEvent?.StatusEventData?.statusEventType) {
            STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT -> {
                when (statusEvent.StatusEventData?.HandheldTriggerEventData?.handheldEvent) {
                    HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED -> {
                        Log.d("TSS", "HANDHELD_TRIGGER_PRESSED")
                        if (reader.isConnected) {
                            try {
                                reader.Actions.Inventory.perform()
                                eventListener?.invoke(DeviceEvent.TriggerPressed)
                            } catch (e: InvalidUsageException) {
                                Log.e(
                                    "TSS",
                                    "Vendor: ${e.vendorMessage}, Message: ${e.message}, Cause: ${e.cause}"
                                )
                            } catch (e: InvalidUsageException) {
                                Log.e(
                                    "TSS",
                                    "Vendor: ${e.vendorMessage}, Message: ${e.message}, Cause: ${e.cause}"
                                )
                            } catch (e: Exception) {
                                Log.e("TSS", "Message: ${e.message}, Cause: ${e.cause}")
                            }
                        }
                    }

                    HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED -> {
                        Log.d("TSS", "HANDHELD_TRIGGER_RELEASED")
                        if (reader.isConnected) {
                            try {
                                reader.Actions.Inventory.stop()
                                reader.Actions.purgeTags()
                                eventListener?.invoke(DeviceEvent.IsTriggerReleased(true))
                            } catch (e: InvalidUsageException) {
                                Log.e(
                                    "TSS",
                                    "Vendor: ${e.vendorMessage}, Message: ${e.message}, Cause: ${e.cause}"
                                )
                            } catch (e: InvalidUsageException) {
                                Log.e(
                                    "TSS",
                                    "Vendor: ${e.vendorMessage}, Message: ${e.message}, Cause: ${e.cause}"
                                )
                            } catch (e: Exception) {
                                Log.e("TSS", "Message: ${e.message}, Cause: ${e.cause}")
                            }
                        }
                    }
                }
            }
            STATUS_EVENT_TYPE.BATTERY_EVENT -> {
                val battery = statusEvent.StatusEventData.BatteryData.level
                Log.d("TSS", "🔋 Battery event received: $battery%")

                try {
                    deviceManager.updateLastBatteryLevel(battery)
                    eventListener?.invoke(DeviceEvent.BatteryData(batteryLevel = battery))
                } catch (e: Exception) {
                    Log.e("TSS", "⚠️ Failed to process battery event: ${e.message}", e)
                }
            }

            STATUS_EVENT_TYPE.DISCONNECTION_EVENT -> {
                Log.d("TSS", "DISCONNECTION_EVENT")
                eventListener?.invoke(DeviceEvent.Disconnected)
                reader.disconnect()
            }
        }
    }
}