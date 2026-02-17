package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val locationChangeRepository: LocationChangeRepository
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
                    val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val model = LocationChangeResultCsvModel(
                        ledgerItemId = ledgerId,
                        locationId = locationId,
                        deviceId = Build.ID,
                        memo = memo,
                        sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                        scannedAt = scannedAt,
                        executedAt = executedAt
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
            var sessionId = 0
            locationChangeRepository.saveLocationChangeTransaction {

                sessionId = locationChangeRepository.createLocationChangeSession(executedAt = executedAt)

                locationChangeRepository.insertLocationChangeEvent(
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