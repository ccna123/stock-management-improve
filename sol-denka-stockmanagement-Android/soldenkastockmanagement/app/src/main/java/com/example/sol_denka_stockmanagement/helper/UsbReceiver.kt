package com.example.sol_denka_stockmanagement.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsbReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            ACTION_USB_STATE -> {
                val connected = intent.getBooleanExtra("connected", false)
                val mtp = intent.getBooleanExtra("mtp", false)

                val state = UsbState(
                    connected = connected,
                    mtp = mtp
                )

                GlobalScope.launch {
                    UsbEvent.emit(state)
                }
            }
        }
    }
}