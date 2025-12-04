package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanDataTable
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeEventRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val locationRepository: LocationRepository,
    private val locationChangeSessionRepository: LocationChangeSessionRepository,
    private val locationChangeEventRepository: LocationChangeEventRepository
) : ViewModel() {
    private val _locationChangeList =
        MutableStateFlow<List<LocationChangeScanDataTable>>(emptyList())
    val locationChangeList = _locationChangeList.asStateFlow()

    private val csvModels = mutableListOf<LocationChangeResultCsvModel>()

    fun getTagDetailForLocationChange(selectedTags: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailList = tagMasterRepository.getTagDetailForLocationChange(selectedTags)
            _locationChangeList.value = detailList
        }
    }

    suspend fun generateCsvData(
        memo: String,
        newLocation: String
    ): List<LocationChangeResultCsvModel> =
        withContext(Dispatchers.IO) {
            try {
                csvModels.clear()
                val newLocationId = locationRepository.getLocationIdByName(newLocation)
                _locationChangeList.value.forEach { row ->
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

    suspend fun saveLocationChangeToDb(memo: String, newLocation: String) {
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
                    _locationChangeList.value.forEach { row ->
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