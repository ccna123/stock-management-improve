package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.tag.TagRepository
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.model.inbound.InboundScanResult
import com.example.sol_denka_stockmanagement.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val tagRepository: TagRepository
) : ViewModel() {

    val scannedTags = readerController.scannedTags

    private val _scanMode = MutableStateFlow(ScanMode.INBOUND)

    // Tag detail for inbound scan
    private val _inboundDetail = MutableStateFlow<InboundScanResult?>(null)
    val inboundDetail = _inboundDetail.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            scannedTags.collect { scannedTags ->
                when (_scanMode.value) {
                    ScanMode.INBOUND -> {
                        val latestTag = scannedTags.values.lastOrNull() ?: return@collect
                        getTagDetailForInbound(latestTag.rfidNo)
                    }

                    ScanMode.OUTBOUND -> {}
                    ScanMode.LOCATION_CHANGE -> {}
                }
            }
        }
    }

    fun setScanMode(mode: ScanMode) {
        _scanMode.value = mode
    }


    suspend fun startInventory() {
        readerController.startInventory()
    }

    suspend fun stopInventory() {
        readerController.stopInventory()
    }

    fun clearScannedTag(){
        readerController.clearScannedTag()
        _inboundDetail.value = null
    }

    fun setEnableScan(enabled: Boolean, screen: Screen = Screen.Inbound) =
        readerController.setScanEnabled(enabled, screen = screen)

    private fun getTagDetailForInbound(epc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val detail = tagRepository.getTagDetailForInbound(epc)
            _inboundDetail.value = InboundScanResult(
                epc = detail.epc,
                itemName = detail.itemName,
                itemCode = detail.itemCode,
                timeStamp = LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        }
    }
}