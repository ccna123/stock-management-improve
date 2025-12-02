package com.example.sol_denka_stockmanagement.screen.inventory.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InventoryCompleteViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _wrongLocationCount = MutableStateFlow(0)
    val wrongLocationCount: StateFlow<Int> = _wrongLocationCount.asStateFlow()

    fun computeWrongLocation(rfidTagList: List<TagMasterModel>, locationName: String) {
        viewModelScope.launch {
            val currentLocationId = getLocationIdByName(locationName)
            val count = rfidTagList.count { tag ->
                isWrongLocation(epc = tag.epc, selectedLocationId = currentLocationId)
            }
            _wrongLocationCount.value = count
        }
    }

    private suspend fun isWrongLocation(epc: String, selectedLocationId: Int): Boolean {
        val tagLocationId = tagMasterRepository.getLocationIdByTag(epc)
        return tagLocationId == null || selectedLocationId != tagLocationId
    }

    private suspend fun getLocationIdByName(locationName: String): Int {
        return locationRepository.getLocationIdByName(locationName = locationName) ?: 0
    }
}