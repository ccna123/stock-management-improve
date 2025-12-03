package com.example.sol_denka_stockmanagement.screen.outbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.outbound.OutboundScanDataTable
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.associateBy
import kotlin.collections.filter
import kotlin.collections.map

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository
) : ViewModel() {

    private val _outboundList = MutableStateFlow<List<OutboundScanDataTable>>(emptyList())
    val outboundList = _outboundList.asStateFlow()

    fun loadOutboundItems(
        checkedMap: Map<String, Boolean>,
        processTypeMap: Map<String, String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val selectedEpcList = checkedMap
                .filter { it.value }
                .map { it.key }

            if (selectedEpcList.isEmpty()) {
                _outboundList.value = emptyList()
                return@launch
            }

            val detailList = tagMasterRepository.getItemNameByTagId(selectedEpcList) ?: emptyList()
            val detailMap = detailList.associateBy { it.epc }


            val result = selectedEpcList.map { epc ->
                val detail = detailMap[epc]

                OutboundScanDataTable(
                    epc = epc,
                    itemName = detail?.itemName ?: "",
                    processType = processTypeMap[epc] ?: "不明"
                )
            }

            _outboundList.value = result
        }
    }
}