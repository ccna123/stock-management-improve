package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
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
    private val locationMasterRepository: LocationMasterRepository,
    private val locationChangeRepository: LocationChangeRepository
) : ViewModel() {

    private val csvModels = mutableListOf<LocationChangeResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ): List<LocationChangeResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                rfidTagList.forEach { tag ->
                    val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
                    val model = LocationChangeResultCsvModel(
                        ledgerItemId = ledgerId ?: 0,
                        locationId = locationId,
                        deviceId = Build.ID,
                        memo = memo,
                        scannedAt = generateIso8601JstTimestamp(),
                        executedAt = generateIso8601JstTimestamp()
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
        rfidTagList: List<TagMasterModel>
    ): Result<Int> {
        return try {
            var sessionId = 0
            locationChangeRepository.saveLocationChangeTransaction {

                sessionId = locationChangeRepository.createLocationChangeSession()

                locationChangeRepository.insertLocationChangeEvent(
                    sessionId = sessionId,
                    memo = memo,
                    locationId = locationId,
                    rfidTagList = rfidTagList
                )
            }
            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}