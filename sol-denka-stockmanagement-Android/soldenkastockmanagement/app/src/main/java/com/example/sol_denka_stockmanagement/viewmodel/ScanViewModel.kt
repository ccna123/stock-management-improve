package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.repository.InventoryItemMasterRepository
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val inventoryItemMasterRepository: InventoryItemMasterRepository,
) : ViewModel() {

    val scannedTag2 = readerController.scannedTags2
    val scannedTags3 = readerController.scannedTags3
    private val _rfidTagList = MutableStateFlow<List<InventoryItemMasterModel>>(emptyList())
    val rfidTagList = _rfidTagList.asStateFlow()

    init {

        viewModelScope.launch {
            inventoryItemMasterRepository.get().collect {
                _rfidTagList.value = it
            }
        }

        viewModelScope.launch {
            readerController.scannedTags1.collect { scannedTag ->
                scannedTag.forEach { (epc, rssi) ->
                    updateTagRssi(epc, rssi)
                }
            }
        }

        viewModelScope.launch {
            readerController.scannedTags.collect { scannedTag ->
                scannedTag.keys.forEach { epc ->
                    updateTagStatus(epc, TagStatus.PROCESSED)
                }
            }
        }
    }

    suspend fun startInventory() {
        readerController.startInventory()
    }

    suspend fun stopInventory() {
        readerController.stopInventory()
    }

    fun clearScannedTag() = readerController.clearScannedTag()

    fun setEnableScan(enabled: Boolean, screen: Screen = Screen.Receiving) =
        readerController.setScanEnabled(enabled, screen = screen)

    fun updateTagRssi(epc: String, newRssi: Float) {
        _rfidTagList.update { list ->
            list.map { item ->
                if (item.epc == epc) {
                    item.copy(newField = item.newField.copy(rssi = newRssi))
                } else {
                    item
                }
            }
        }
    }

    fun updateTagStatus(epc: String, newStatus: TagStatus) {
        _rfidTagList.update { list ->
            list.map { item ->
                if (item.epc == epc) {
                    item.copy(newField = item.newField.copy(tagStatus = newStatus))
                } else {
                    item
                }
            }
        }
    }

    fun clearProcessedTag() {
        _rfidTagList.update { list ->
            list.map { item ->
                if (item.newField.tagStatus == TagStatus.PROCESSED) {
                    item.copy(newField = item.newField.copy(tagStatus = TagStatus.UNPROCESSED))
                } else item
            }
        }
    }
}