package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.controller.TagController
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val tagController: TagController,
    private val ledgerItemRepository: LedgerItemRepository,
    private val tagMasterRepository: TagMasterRepository
) : ViewModel() {

    val scannedTags = readerController.scannedTags

    private val _scanMode = MutableStateFlow(ScanMode.INBOUND)

    private val _rfidTagList = MutableStateFlow<List<TagMasterModel>>(emptyList())
    val rfidTagList = _rfidTagList.asStateFlow()

    private val _rssiMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val rssiMap = _rssiMap.asStateFlow()

    private val _lastInboundEpc = MutableStateFlow<String?>(null)
    val lastInboundEpc = _lastInboundEpc.asStateFlow()


    private var inboundJob: Job? = null
    private var outboundJob: Job? = null
    private var inventoryJob: Job? = null
    private var processJob: Job? = null
    private var searchJob: Job? = null

    init {

        viewModelScope.launch(Dispatchers.IO) {

            val tagFlow = tagMasterRepository.get()
            val mappedTagIdFlow = ledgerItemRepository.getMappedTagIdsFlow()

            val fullInfoFlow = ledgerItemRepository.get()
                .map { tagMasterRepository.getFullInfo() } // fullInfo list
                .distinctUntilChanged()

            combine(tagFlow, mappedTagIdFlow,  fullInfoFlow) { tagList, mappedTagIds, fullInfoList ->
                val mappedSet = mappedTagIds.toSet()
                val infoMap = fullInfoList.associateBy { it.epc }
                val prevMap = _rfidTagList.value.associateBy { it.epc }

                tagList.map { tag ->
                    val prev = prevMap[tag.epc]
                    val info = infoMap[tag.epc]

                    tag.copy(
                        newFields = tag.newFields.copy(

                            hasLeger = tag.tagId in mappedSet,

                            itemName = info?.itemName ?: "",
                            itemCode = info?.itemCode ?: "",
                            location = info?.location ?: "",

                            tagStatus = prev?.newFields?.tagStatus ?: TagStatus.UNPROCESSED,
                            rssi = prev?.newFields?.rssi ?: -100f,
                            isChecked = prev?.newFields?.isChecked ?: false,
                            processType = prev?.newFields?.processType ?: ""
                        )
                    )
                }
            }.collect { merged ->
                _rfidTagList.value = merged
            }
        }
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
                                val latest = tags.values.lastOrNull()?.rfidNo ?: return@collect
                                _lastInboundEpc.value = latest
                                readerController.clearScannedTag()
                            }
                        }
                    }

                    ScanMode.INVENTORY_SCAN -> {
                        searchJob?.cancel()
                        inboundJob?.cancel()
                        inventoryJob = viewModelScope.launch(Dispatchers.IO) {
                            tagController.statusMap.collect { status ->
                                val updated = _rfidTagList.value.map { item ->
                                    val s = status[item.epc] ?: TagStatus.UNPROCESSED
                                    item.copy(
                                        newFields = item.newFields.copy(
                                            tagStatus = s
                                        )
                                    )
                                }
                                _rfidTagList.value = updated
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
                            scannedTags.collect {}
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
        _lastInboundEpc.value = ""
    }

    fun setEnableScan(enabled: Boolean) = readerController.setScanEnabled(enabled)

    fun updateTagStatus(epc: String, status: TagStatus) {
        tagController.updateTagStatus(epc, status)
    }

    fun clearTagStatusAndRssi() {
        readerController.clearScannedTag()
        tagController.clearTagStatusAndRssi()
    }

    fun applyProcessType(processTypeMap: Map<String, String>) {
        val currentList = _rfidTagList.value

        val updated = currentList.map { tag ->
            val newProcessType = processTypeMap[tag.epc] ?: tag.newFields.processType
            tag.copy(newFields = tag.newFields.copy(processType = newProcessType))
        }
        _rfidTagList.value = updated
    }

    fun toggleCheck(epc: String) {
        val updated = _rfidTagList.value.map { tag ->
            if (tag.epc == epc) {
                tag.copy(newFields = tag.newFields.copy(isChecked = !tag.newFields.isChecked))
            } else tag
        }
        _rfidTagList.value = updated
    }

    fun toggleCheckAll(value: Boolean, targetEpcs: Set<String> = emptySet()) {
        val updated = _rfidTagList.value.map { tag ->
            if (tag.epc in targetEpcs) {
                tag.copy(newFields = tag.newFields.copy(isChecked = value))
            } else {
                tag
            }
        }
        _rfidTagList.value = updated
    }

    fun resetIsCheckedField() {
        val updated = _rfidTagList.value.map { tag ->
            tag.copy(newFields = tag.newFields.copy(isChecked = false))
        }
        _rfidTagList.value = updated
    }
}