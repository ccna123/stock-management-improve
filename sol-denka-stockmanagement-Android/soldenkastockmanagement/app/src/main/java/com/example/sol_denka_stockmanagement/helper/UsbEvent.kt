package com.example.sol_denka_stockmanagement.helper

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class UsbState(
    val connected: Boolean,
    val mtp: Boolean,
)

object UsbEvent {
    private val _usbEvents = MutableSharedFlow<UsbState>(replay = 1)
    val usbEvents = _usbEvents.asSharedFlow()

    suspend fun emit(state: UsbState){
        _usbEvents.emit(state)
    }










}