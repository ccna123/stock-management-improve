package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanDataTable
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.csv.LocationChangeResultCsvModel
import com.example.sol_denka_stockmanagement.model.csv.OutboundResultCsvModel
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
    private val locationRepository: LocationRepository
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
    suspend fun generateCsvData(memo: String, newLocation: String): List<LocationChangeResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()

            _locationChangeList.value.forEach { row ->
                val locationId = locationRepository.getLocationIdByName(newLocation)
                val ledgerId = tagMasterRepository.getLedgerIdByEpc(row.epc)
                val model = LocationChangeResultCsvModel(
                    ledgerItemId = ledgerId ?: 0,
                    locationId = locationId ?: 0,
                    deviceId = Build.ID,
                    memo = memo,
                    scannedAt = generateIso8601JstTimestamp(),
                    executedAt = generateIso8601JstTimestamp()
                )
                csvModels.add(model)
            }
            csvModels.toList()
        }
}