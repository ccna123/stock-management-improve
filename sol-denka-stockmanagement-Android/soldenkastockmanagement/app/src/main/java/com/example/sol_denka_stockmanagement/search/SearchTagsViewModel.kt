package com.example.sol_denka_stockmanagement.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ITagOperation
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.helper.TagController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class SearchTagsViewModel @Inject constructor(
    private val tagController: TagController,
    private val readerController: ReaderController
) : ViewModel(), ITagOperation {

    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val rssiMap = _rssiMap.asStateFlow()

    init {
        viewModelScope.launch {
            readerController.scannedTags.collect { newMap ->
                _rssiMap.value = newMap.mapValues { it.value.rssi }
            }
        }
    }

    override fun updateTagStatus(epc: String, status: TagStatus) {
        tagController.updateTagStatus(epc, status)
    }

    override fun updateRssi(epc: String, rssi: Float) {
        tagController.updateRssi(epc, rssi)
    }

    override fun clearAll() {
        tagController.clearAll()
    }
}