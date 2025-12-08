package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.sqlite.SQLiteException
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val outboundRepository: OutboundRepository
) : ViewModel() {

    private val csvModels = mutableListOf<OutboundResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        rfidTagList: List<TagMasterModel>
    ): List<OutboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            rfidTagList.forEach { tag ->
                val processTypeId = processTypeRepository.getIdByName(tag.newFields.processType)
                val (tagId, ledgerId) = tagMasterRepository.getTagIdLedgerIdByEpc(tag.epc)
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

    suspend fun saveOutboundToDb(
        memo: String,
        occurredAt: String,
        rfidTagList: List<TagMasterModel>
    ): Result<Int> {
        return try {
            val sessionId = outboundRepository.saveOutbound(
                memo = memo,
                occurredAt = occurredAt,
                tags = rfidTagList
            )
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}