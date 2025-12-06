package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeEventRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val locationRepository: LocationRepository,
    private val locationChangeSessionRepository: LocationChangeSessionRepository,
    private val locationChangeEventRepository: LocationChangeEventRepository
) : ViewModel() {

    private val csvModels = mutableListOf<LocationChangeResultCsvModel>()

    suspend fun generateCsvData(
        memo: String,
        newLocation: String,
        rfidTagList: List<TagMasterModel>
    ): List<LocationChangeResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                val newLocationId = locationRepository.getLocationIdByName(newLocation)
                rfidTagList.forEach { row ->
                    val ledgerId = tagMasterRepository.getLedgerIdByEpc(row.epc)
                    val model = LocationChangeResultCsvModel(
                        ledgerItemId = ledgerId ?: 0,
                        locationId = newLocationId ?: 0,
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

    suspend fun saveLocationChangeToDb(memo: String, newLocation: String, rfidTagList: List<TagMasterModel>) {
        withContext(Dispatchers.IO) {
            try {
                val sessionId = locationChangeSessionRepository.insert(
                    LocationChangeSessionModel(
                        deviceId = Build.ID,
                        executedAt = generateIso8601JstTimestamp()
                    )
                )
                val newLocationId = locationRepository.getLocationIdByName(newLocation)
                sessionId.let {
                    rfidTagList.forEach { row ->
                        val ledgerId = tagMasterRepository.getLedgerIdByEpc(row.epc)
                        val model = LocationChangeEventModel(
                            locationChangeSessionId = sessionId.toInt(),
                            ledgerItemId = ledgerId ?: 0,
                            locationId = newLocationId ?: 0,
                            memo = memo,
                            scannedAt = generateIso8601JstTimestamp(),
                        )
                        locationChangeEventRepository.insert(model)
                    }
                }
            } catch (e: Exception) {
                Log.e("TSS", "saveLocationChangeToDb: ${e.message}")
            }
        }
    }
}