package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ITagOperation
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.controller.TagController
import com.example.sol_denka_stockmanagement.model.inbound.InboundScanResult
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val tagController: TagController,
    private val tagMasterRepository: TagMasterRepository
) : ViewModel(), ITagOperation {

    val scannedTags = readerController.scannedTags

    private val _scanMode = MutableStateFlow(ScanMode.INBOUND)

    // Tag detail for inbound scan
    private val _inboundDetail = MutableStateFlow<InboundScanResult?>(null)
    val inboundDetail = _inboundDetail.asStateFlow()

    private val _epcNameMap = MutableStateFlow<Map<String, String?>>(emptyMap())
    val epcNameMap = _epcNameMap.asStateFlow()

    private val _rfidTagList = MutableStateFlow<List<TagMasterModel>>(emptyList())
    val rfidTagList = _rfidTagList.asStateFlow()

    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val rssiMap = _rssiMap.asStateFlow()

    private var inboundJob: Job? = null
    private var outboundJob: Job? = null
    private var inventoryJob: Job? = null
    private var processJob: Job? = null
    private var searchJob: Job? = null

    init {

        // 1) always collect scanMode
        viewModelScope.launch(Dispatchers.IO) {
            _scanMode.collect { mode ->

                // cancel all jobs whenever mode changes
                inboundJob?.cancel()
                outboundJob?.cancel()
                inventoryJob?.cancel()
                processJob?.cancel()
                searchJob?.cancel()

                when (mode) {

                    ScanMode.INBOUND -> {
                        inboundJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { tags ->
                                val latest = tags.values.lastOrNull() ?: return@collect
                                getTagDetailForInbound(latest.rfidNo)
                                readerController.clearScannedTag()
                            }
                        }
                    }

                    ScanMode.INVENTORY_SCAN -> {
                        searchJob?.cancel()
                        inboundJob?.cancel()
                        inventoryJob = viewModelScope.launch(Dispatchers.IO) {
                            combine(
                                tagMasterRepository.get(),
                                tagController.statusMap
                            ) { master, status ->
                                master.map { item ->
                                    val s = status[item.epc] ?: item.newFields.tagStatus
                                    item.copy(newFields = item.newFields.copy(tagStatus = s))
                                }
                            }.collect { merged ->
                                _rfidTagList.value = merged


                                val detailList =
                                    tagMasterRepository.getItemNameByTagId(merged.map { it.epc }) ?: emptyList()

                                val map = detailList.associate { it.epc to it.itemName }

                                _epcNameMap.value = map
                            }
                        }

                        processJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { scanned ->
                                scanned.keys.forEach { epc ->
                                    updateTagStatus(epc, TagStatus.PROCESSED)
                                }
                            }
                        }
                    }

                    ScanMode.SEARCH -> {
                        searchJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { map ->
                                _rssiMap.value = map.mapValues { it.value.rssi }
                            }
                        }
                    }

                    ScanMode.OUTBOUND, ScanMode.LOCATION_CHANGE -> {
                        outboundJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { tags ->

                                val epcList = tags.keys.toList()
                                if (epcList.isEmpty()) return@collect

                                val detailList =
                                    tagMasterRepository.getItemNameByTagId(epcList) ?: emptyList()

                                val map = detailList.associate { it.epc to it.itemName }

                                _epcNameMap.value = map
                            }
                        }
                    }
                    ScanMode.NONE -> {}
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

    fun clearScannedTag() {
        readerController.clearScannedTag()
    }

    fun clearInboundDetail() {
        _inboundDetail.value = null
    }

    fun setEnableScan(enabled: Boolean) = readerController.setScanEnabled(enabled)

    private fun getTagDetailForInbound(epc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val detail = tagMasterRepository.getTagDetailForInbound(epc)
            if (detail != null) {
                _inboundDetail.value = InboundScanResult(
                    epc = detail.epc,
                    itemName = detail.itemName,
                    itemCode = detail.itemCode,
                    timeStamp = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
        }
    }

    override fun updateTagStatus(epc: String, status: TagStatus) {
        tagController.updateTagStatus(epc, status)
    }

    override fun updateRssi(epc: String, rssi: Float) {
        tagController.updateRssi(epc, rssi)
    }

    override fun clearTagStatusAndRssi() {
        readerController.clearScannedTag()
        tagController.clearTagStatusAndRssi()
    }
}