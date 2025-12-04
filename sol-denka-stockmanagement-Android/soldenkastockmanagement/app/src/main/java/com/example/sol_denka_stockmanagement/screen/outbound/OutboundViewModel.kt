package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundEventRepository
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.model.outbound.OutBoundEventModel
import com.example.sol_denka_stockmanagement.model.outbound.OutboundScanDataTable
import com.example.sol_denka_stockmanagement.model.outbound.OutboundSessionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.associateBy
import kotlin.collections.filter
import kotlin.collections.map

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val outboundSessionRepository: OutboundSessionRepository,
    private val outboundEventRepository: OutboundEventRepository
) : ViewModel() {

    private val _outboundList = MutableStateFlow<List<OutboundScanDataTable>>(emptyList())
    val outboundList = _outboundList.asStateFlow()

    private val csvModels = mutableListOf<OutboundResultCsvModel>()

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

    suspend fun generateCsvData(memo: String): List<OutboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            _outboundList.value.forEach { row ->
                val processTypeId = processTypeRepository.getIdByName(row.processType)
                val (tagId, ledgerId) = tagMasterRepository.getTagIdLedgerIdByEpc(row.epc)
                val model = OutboundResultCsvModel(
                    ledgerItemId = ledgerId ?: 0,
                    tagId = tagId,
                    processTypeId = processTypeId,
                    deviceId = Build.ID,
                    memo = memo,
                    occurredAt = generateIso8601JstTimestamp(),
                    registeredAt = generateIso8601JstTimestamp()
                )
                csvModels.add(model)
            }
            csvModels.toList()
        }

    suspend fun saveOutboundToDb(memo: String, occurredAt: String) {
        withContext(Dispatchers.IO) {
            try {
                val sessionId = outboundSessionRepository.insert(
                    OutboundSessionModel(
                        deviceId = Build.ID,
                        executedAt = generateIso8601JstTimestamp(),
                    )
                )
                sessionId.let {
                    _outboundList.value.forEach { row ->
                        val ledgerId = tagMasterRepository.getLedgerIdByEpc(row.epc)
                        val processTypeId = processTypeRepository.getIdByName(row.processType)
                        val model = OutBoundEventModel(
                            outboundSessionId = sessionId.toInt(),
                            ledgerItemId = ledgerId ?: 0,
                            processTypeId = processTypeId,
                            memo = memo,
                            occurredAt = occurredAt,
                            registeredAt = generateIso8601JstTimestamp()
                        )
                        outboundEventRepository.insert(model)
                    }
                }
            } catch (e: Exception) {
                Log.e("TSS", "saveOutboundToDb: ${e.message}")
            }
        }
    }
}