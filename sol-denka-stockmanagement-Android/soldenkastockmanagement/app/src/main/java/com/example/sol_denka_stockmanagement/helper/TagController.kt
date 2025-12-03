package com.example.sol_denka_stockmanagement.helper

import com.example.sol_denka_stockmanagement.app_interface.ITagOperation
import com.example.sol_denka_stockmanagement.constant.TagStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

@Singleton
class TagController: ITagOperation {
    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val rssiMap = _rssiMap.asStateFlow()

    private val _statusMap = MutableStateFlow<Map<String, TagStatus>>(emptyMap())
    val statusMap = _statusMap.asStateFlow()

    override fun updateTagStatus(epc: String, status: TagStatus) {
        _statusMap.update { it + (epc to status) }
    }

    override fun updateRssi(epc: String, rssi: Float) {
        _rssiMap.update { it + (epc to rssi) }
    }

    override fun clearTagStatusAndRssi() {
        _statusMap.value = emptyMap()
        _rssiMap.value = emptyMap()
    }
}