package com.example.sol_denka_stockmanagement.domain.usecase.location

import android.os.Build
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.domain.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveLocationChangeUseCase @Inject constructor(
    private val locationChangeRepo: ILocationChangeRepository,
    private val tagRepo: ITagMasterRepository
) {
    suspend fun saveToDb(
        memo: String,
        locationId: Int,
        scannedAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): Result<Unit> = runCatching {
        locationChangeRepo.saveLocationChangeTransaction {
            val sessionId = locationChangeRepo.createLocationChangeSession(executedAt)
            locationChangeRepo.insertLocationChangeEvent(
                sessionId = sessionId,
                memo = memo,
                locationId = locationId,
                sourceEventIdByTagId = sourceEventIdByTagId,
                scannedAt = scannedAt,
                rfidTagList = rfidTagList
            )
        }
    }

    suspend fun generateCsvData(
        memo: String,
        locationId: Int,
        scannedAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): List<LocationChangeResultCsvModel> = withContext(Dispatchers.IO) {
        rfidTagList.mapNotNull { tag ->
            runCatching {
                val ledgerId = tagRepo.getLedgerIdByTagId(tag.tagId)
                LocationChangeResultCsvModel(
                    ledgerItemId = ledgerId,
                    locationId = locationId,
                    deviceId = Build.ID,
                    memo = memo,
                    sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                    scannedAt = scannedAt,
                    executedAt = executedAt,
                    timeStamp = formatTimestamp(executedAt)
                )
            }.getOrNull()
        }
    }
}