package com.example.sol_denka_stockmanagement

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("TSS", "MyApplication onCreate")
    }
}