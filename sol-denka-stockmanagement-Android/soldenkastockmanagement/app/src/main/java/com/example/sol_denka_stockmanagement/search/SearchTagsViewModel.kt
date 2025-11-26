package com.example.sol_denka_stockmanagement.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.ReaderController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class SearchTagsViewModel @Inject constructor(
    private val readerController: ReaderController
) : ViewModel() {

    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val rssiMap = _rssiMap.asStateFlow()

    init {
        viewModelScope.launch {
            readerController.scannedTags1.collect { newMap ->
                _rssiMap.value = newMap
            }
        }
    }

    fun getRssi(epc: String): Float {
        return _rssiMap.value[epc] ?: -100f
    }
}