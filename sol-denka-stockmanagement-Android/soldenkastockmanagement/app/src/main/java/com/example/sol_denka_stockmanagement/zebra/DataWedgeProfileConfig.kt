package com.example.sol_denka_stockmanagement.zebra

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

object DataWedgeProfileConfig {
    private const val PROFILE_NAME = "TSS_STOCK_MANAGEMENT"
    private const val DW_ACTION = "com.symbol.datawedge.api.ACTION"
    private const val DW_PACKAGE = "com.symbol.datawedge"

    fun setupProfile(context: Context) {
        Log.i("TSS", "Setting up DataWedge 8.2 profile")

        GlobalScope.launch(Dispatchers.IO) {
            enableDW(context)
            delay(300)
            deleteProfile(context)
            delay(300)
            createProfile(context)
            delay(400)
            switchProfile(context)
            delay(400)
            applyIntentOutputConfig(context)

        }
    }

    /** Enable DataWedge service */
    private fun enableDW(context: Context) {
        val i = Intent(DW_ACTION).apply {
            setPackage(DW_PACKAGE)
            putExtra("com.symbol.datawedge.api.ENABLE_DATAWEDGE", true)
        }
        context.sendBroadcast(i)
        Log.i("TSS", "ENABLE_DATAWEDGE sent")
    }

    /** Create the profile */
    private fun createProfile(context: Context) {
        val i = Intent(DW_ACTION).apply {
            setPackage(DW_PACKAGE)
            putExtra("com.symbol.datawedge.api.CREATE_PROFILE", PROFILE_NAME)
        }
        context.sendBroadcast(i)
        Log.i("TSS", "CREATE_PROFILE sent")
    }

    /** Switch to it before applying config */
    private fun switchProfile(context: Context) {
        val i = Intent(DW_ACTION).apply {
            setPackage(DW_PACKAGE)
            putExtra("com.symbol.datawedge.api.SWITCH_TO_PROFILE", PROFILE_NAME)
        }
        context.sendBroadcast(i)
        Log.i("TSS", "SWITCH_TO_PROFILE sent")
    }
    /** Delete the profile if it exists */
    private fun deleteProfile(context: Context) {
        val i = Intent(DW_ACTION).apply {
            setPackage(DW_PACKAGE)
            putExtra("com.symbol.datawedge.api.DELETE_PROFILE", PROFILE_NAME)
        }
        context.sendBroadcast(i)
        Log.i("TSS", "DELETE_PROFILE sent for $PROFILE_NAME")
    }

    /** Apply the official Intent Output plugin config (from Zebra 8.2 docs) */
    private fun applyIntentOutputConfig(context: Context) {
        Log.i("TSS", "Applying Intent Output config (DW 8.2 example)")

        // Construct the full profile configuration bundle
        val profileConfig = Bundle().apply {
            putString("PROFILE_NAME", PROFILE_NAME)
            putString("PROFILE_ENABLED", "true")
            putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")

            // Associate this app
            val appConfig = Bundle().apply {
                putString("PACKAGE_NAME", context.packageName)
                putStringArray("ACTIVITY_LIST", arrayOf("*"))
            }
            putParcelableArray("APP_LIST", arrayOf(appConfig))

            // --- Disable Keystroke Output ---
            val keyConfig = Bundle().apply {
                putString("PLUGIN_NAME", "KEYSTROKE")
                val params = Bundle().apply {
                    putString("keystroke_output_enabled", "false")
                }
                putBundle("PARAM_LIST", params)
            }

            // Input plugin (BARCODE) required by DW
            val barcodeConfig = Bundle().apply {
                putString("PLUGIN_NAME", "BARCODE")
                putString("RESET_CONFIG", "false")
                // Optional: enable specific symbologies if you want
                val barcodeParams = Bundle().apply {
                    putString("scanner_selection", "auto")
                    putString("scanner_input_enabled", "true") // enables Barcode Input
                    putString("decoder_code128", "true")
                    putString("decoder_code39", "true")
                    putString("decoder_ean13", "true")
                    putString("decoder_qr", "true")
                }
                putBundle("PARAM_LIST", barcodeParams)
            }

            // --- Enable Intent Output ---
            val intentConfig = Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "false")
                val params = Bundle().apply {
                    putString("intent_output_enabled", "true")
                    putString("intent_action", "com.example.sol_denka_stockmanagement.datawedge_action")
                    putString("intent_category", "com.example.sol_denka_stockmanagement.datawedge_category")
                    putInt("intent_delivery", 2) // broadcast
                }
                putBundle("PARAM_LIST", params)
            }


            // Add both plugins
            putParcelableArrayList(
                "PLUGIN_CONFIG",
                arrayListOf(keyConfig, intentConfig, barcodeConfig)
            )
        }

        // Send SET_CONFIG broadcast
        val i = Intent("com.symbol.datawedge.api.ACTION").apply {
            setPackage("com.symbol.datawedge")
            putExtra("com.symbol.datawedge.api.SET_CONFIG", profileConfig)
            putExtra("SEND_RESULT", "LAST_RESULT")
            putExtra("COMMAND_IDENTIFIER", "TSSAPP_INIT")
        }
        context.sendBroadcast(i)
        Log.i("TSS", "SET_CONFIG (Intent Output) broadcast sent for $PROFILE_NAME")
    }
}