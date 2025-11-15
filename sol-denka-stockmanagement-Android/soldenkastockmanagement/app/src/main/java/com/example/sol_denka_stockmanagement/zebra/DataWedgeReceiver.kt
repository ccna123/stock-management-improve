package com.example.sol_denka_stockmanagement.zebra

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ZebraDataWedgeReceiver(private val onBarcode: (data: String, labelType: String?, source: String?) -> Unit): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        // Handle DataWedge intent
        if (intent.action == "com.example.sol_denka_stockmanagement.datawedge_action"){

            val data = intent.getStringExtra("com.symbol.datawedge.data_string")
            val labelType = intent.getStringExtra("com.symbol.datawedge.label_type")
            val source = intent.getStringExtra("com.symbol.datawedge.source")

            if (!data.isNullOrEmpty()) {
                Log.d("TSS", "ZebraDataWedgeReceiver: Barcode=$data, Type=$labelType, Source=$source")
                onBarcode(data, labelType, source)
            }
        }
    }
}