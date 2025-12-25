package com.example.sol_denka_stockmanagement.helper.controller

import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

@Singleton
class TagController {
    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())

    private val _statusMap = MutableStateFlow<Map<String, TagScanStatus>>(emptyMap())
    val statusMap = _statusMap.asStateFlow()

    fun updateTagStatus(epc: String, status: TagScanStatus) {
        _statusMap.update { it + (epc to status) }
    }

    fun updateRssi(epc: String, rssi: Float) {
        _rssiMap.update { it + (epc to rssi) }
    }

    fun clearTagStatusAndRssi() {
        _statusMap.value = emptyMap()
        _rssiMap.value = emptyMap()
    }
}