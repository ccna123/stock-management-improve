package com.example.sol_denka_stockmanagement.screen.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ITagOperation
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.repository.tag.TagRepository
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.helper.TagController
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val tagController: TagController,
    private val readerController: ReaderController,
    private val tagRepository: TagRepository
) : ViewModel(), ITagOperation {

    private val _rfidTagList = MutableStateFlow<List<TagMasterModel>>(emptyList())
    val rfidTagList = _rfidTagList.asStateFlow()

    init {

        viewModelScope.launch {
            combine(
                tagRepository.get(),
                tagController.statusMap
            ) { masterList, statusMap ->
                masterList.map { item ->
                    val newStatus = statusMap[item.epc] ?: item.newFields.tagStatus
                    item.copy(
                        newFields = item.newFields.copy(tagStatus = newStatus)
                    )
                }
            }.collect { mergedList ->
                _rfidTagList.value = mergedList
            }
        }
        viewModelScope.launch {
            readerController.scannedTags.collect { scanned ->
                scanned.keys.forEach { epc ->
                    if (_rfidTagList.value.any { it.epc == epc }) {
                        updateTagStatus(epc, TagStatus.PROCESSED)
                    }
                }
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