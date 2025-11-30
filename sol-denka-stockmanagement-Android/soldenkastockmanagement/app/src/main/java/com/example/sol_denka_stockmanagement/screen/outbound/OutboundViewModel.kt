package com.example.sol_denka_stockmanagement.screen.outbound

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanResult
import com.example.sol_denka_stockmanagement.database.dao.tag.TagDao
import com.example.sol_denka_stockmanagement.database.repository.tag.TagRepository
import com.example.sol_denka_stockmanagement.model.outbound.OutboundScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _outboundList = MutableStateFlow<List<OutboundScanResult>>(emptyList())
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

            val detailList = tagRepository.getTagDetailForOutbound(selectedEpcList)
            val detailMap = detailList.associateBy { it.epc }

            val result = selectedEpcList.map { epc ->
                val detail = detailMap[epc]

                OutboundScanResult(
                    epc = epc,
                    itemName = detail?.itemName ?: "",
                    processType = processTypeMap[epc] ?: "不明"
                )
            }

            _outboundList.value = result
        }
    }
}


