package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
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
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.text.get

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
    private var outboundLocationChangeJob: Job? = null
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
                            isInStock = info?.isInStock ?: false,

                            itemName = info?.itemName ?: "",
                            itemCode = info?.itemCode ?: "",
                            location = info?.location ?: "",
                            packingType = info?.packingType ?: "",
                            specificGravity = info?.specificGravity ?: 0,
                            thickness = info?.thickness?.toBigDecimal() ?: BigDecimal.ZERO,
                            length = info?.length ?: 0,
                            width = info?.width ?: 0,
                            weight = info?.weight ?: 0,
                            lotNo = info?.lotNo ?: "",
                            quantity = info?.quantity ?: 0,
                            occurrenceReason = info?.occurrenceReason ?: "",
                            occurredAt = info?.occurredAt ?: "",
                            processedAt = info?.processedAt ?: "",
                            winderName = info?.winderName ?: "",
                            categoryName = info?.categoryName ?: "",

                            tagScanStatus = prev?.newFields?.tagScanStatus ?: TagScanStatus.UNPROCESSED,
                            rssi = prev?.newFields?.rssi ?: -100f,
                            isChecked = prev?.newFields?.isChecked ?: false,
                            processType = prev?.newFields?.processType ?: "",
                            readTimeStamp = prev?.newFields?.readTimeStamp ?: 0L
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
                outboundLocationChangeJob?.cancel()
                inventoryJob?.cancel()
                processJob?.cancel()
                searchJob?.cancel()

                when (mode) {

                    ScanMode.INBOUND -> {
                        outboundLocationChangeJob?.cancel()
                        inventoryJob?.cancel()
                        processJob?.cancel()
                        searchJob?.cancel()
                        inboundJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { tags ->
                                val latest = tags.values.lastOrNull()?.rfidNo ?: return@collect

                                val now = System.currentTimeMillis()

                                _lastInboundEpc.value = latest

                                _rfidTagList.value = _rfidTagList.value.map { tag ->
                                    if (tag.epc == latest) {
                                        tag.copy(
                                            newFields = tag.newFields.copy(
                                                readTimeStamp = now
                                            )
                                        )
                                    } else tag
                                }

                                readerController.clearScannedTag()
                            }
                        }
                    }

                    ScanMode.INVENTORY_SCAN -> {
                        searchJob?.cancel()
                        inboundJob?.cancel()
                        outboundLocationChangeJob?.cancel()
                        inventoryJob = viewModelScope.launch(Dispatchers.IO) {
                            tagController.statusMap.collect { status ->
                                val updated = _rfidTagList.value.map { item ->
                                    val s = status[item.epc] ?: TagScanStatus.UNPROCESSED
                                    item.copy(
                                        newFields = item.newFields.copy(
                                            tagScanStatus = s
                                        )
                                    )
                                }
                                _rfidTagList.value = updated
                            }
                        }


                        processJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { scanned ->
                                scanned.keys.forEach { epc ->
                                    updateTagStatus(epc, TagScanStatus.PROCESSED)
                                }
                            }
                        }
                    }

                    ScanMode.SEARCH -> {
                        inboundJob?.cancel()
                        outboundLocationChangeJob?.cancel()
                        inventoryJob?.cancel()
                        processJob?.cancel()
                        searchJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { map ->
                                _rssiMap.value = map.mapValues { it.value.rssi }
                            }
                        }
                    }

                    ScanMode.OUTBOUND, ScanMode.LOCATION_CHANGE -> {
                        inboundJob?.cancel()
                        inventoryJob?.cancel()
                        processJob?.cancel()
                        searchJob?.cancel()
                        outboundLocationChangeJob = viewModelScope.launch(Dispatchers.IO) {
                            scannedTags.collect { scanned ->

                                val lastEpc = scanned.keys.lastOrNull() ?: return@collect
                                val now = System.currentTimeMillis()

                                val updated = _rfidTagList.value.map { tag ->
                                    if (tag.epc == lastEpc) {
                                        tag.copy(
                                            newFields = tag.newFields.copy(
                                                readTimeStamp = now
                                            )
                                        )
                                    } else tag
                                }

                                _rfidTagList.value =
                                    updated.sortedByDescending { it.newFields.readTimeStamp }
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
        _lastInboundEpc.value = ""
    }

    fun setEnableScan(enabled: Boolean) = readerController.setScanEnabled(enabled)

    fun updateTagStatus(epc: String, status: TagScanStatus) {
        tagController.updateTagStatus(epc, status)
    }

    fun clearTagStatusAndRssi() {
        readerController.clearScannedTag()
        tagController.clearTagStatusAndRssi()
    }

    fun applyProcessType(processTypeMap: Map<String, String>) {
        val updated = _rfidTagList.value.map { tag ->
            if (tag.newFields.hasLeger) {
                val newProcessType = processTypeMap[tag.epc] ?: tag.newFields.processType
                tag.copy(newFields = tag.newFields.copy(processType = newProcessType))
            } else tag
        }

        _rfidTagList.value = updated
    }

    fun toggleCheck(epc: String) {
        val updated = _rfidTagList.value.map { tag ->
            if (tag.epc == epc && tag.newFields.hasLeger) {
                tag.copy(
                    newFields = tag.newFields.copy(
                        isChecked = !tag.newFields.isChecked
                    )
                )
            } else tag
        }

        _rfidTagList.value = updated
    }

    fun toggleCheckAll(value: Boolean, targetEpcs: Set<String> = emptySet()) {
        val updated = _rfidTagList.value.map { tag ->
            if (tag.epc in targetEpcs && tag.newFields.hasLeger) {
                tag.copy(
                    newFields = tag.newFields.copy(isChecked = value)
                )
            } else tag
        }

        _rfidTagList.value = updated
    }

    fun resetIsCheckedField() {
        val updated = _rfidTagList.value.filter { it.newFields.hasLeger }.map { tag ->
            tag.copy(newFields = tag.newFields.copy(isChecked = false))
        }
        _rfidTagList.value = updated
    }
}