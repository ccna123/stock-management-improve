package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val ITagMasterRepository: ITagMasterRepository,
    private val ILocationChangeRepository: com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeRepository
) : ViewModel() {

    private val csvModels = mutableListOf<LocationChangeResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        locationId: Int,
        scannedAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): List<LocationChangeResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                rfidTagList.forEach { tag ->
                    val ledgerId = ITagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val model = LocationChangeResultCsvModel(
                        ledgerItemId = ledgerId,
                        locationId = locationId,
                        deviceId = Build.ID,
                        memo = memo,
                        sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                        scannedAt = scannedAt,
                        executedAt = executedAt,
                        timeStamp = formatTimestamp(executedAt)
                    )
                    csvModels.add(model)
                }
                csvModels.toList()
            } catch (e: Exception) {
                Log.e("TSS", "generateCsvData: ${e.message}")
                emptyList()
            }
        }

    suspend fun saveLocationChangeToDb(
        memo: String,
        locationId: Int,
        scannedAt: String,
        executedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ): Result<Unit> {
        return try {
            var sessionId: Int
            ILocationChangeRepository.saveLocationChangeTransaction {

                sessionId = ILocationChangeRepository.createLocationChangeSession(executedAt = executedAt)

                ILocationChangeRepository.insertLocationChangeEvent(
                    sessionId = sessionId,
                    memo = memo,
                    locationId = locationId,
                    sourceEventIdByTagId = sourceEventIdByTagId,
                    scannedAt = scannedAt,
                    rfidTagList = rfidTagList
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}