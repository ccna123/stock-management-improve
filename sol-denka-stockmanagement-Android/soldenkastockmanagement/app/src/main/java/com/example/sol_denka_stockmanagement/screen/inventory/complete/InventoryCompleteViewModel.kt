package com.example.sol_denka_stockmanagement.screen.inventory.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.TagStatus
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

    private val _okCount = MutableStateFlow(0)
    val okCount = _okCount.asStateFlow()

    private val _shortageCount = MutableStateFlow(0)
    val shortageCount = _shortageCount.asStateFlow()

    private val _overCount = MutableStateFlow(0)
    val overCount = _overCount.asStateFlow()

    fun computeResult(rfidTagList: List<TagMasterModel>, locationName: String) {
        viewModelScope.launch {
            val currentLocationId = getLocationIdByName(locationName)
            val count = rfidTagList.count { tag ->
                isWrongLocation(epc = tag.epc, selectedLocationId = currentLocationId)
            }
            _wrongLocationCount.value = count

            // shortage
            val tagsInStock = tagMasterRepository.getTagsByLocationAndStock(
                locationId = currentLocationId,
                isInStock = true
            )

            val scannedTags = rfidTagList.map { it.epc }.toMutableSet()
            val shortage = tagsInStock.count { it.epc !in scannedTags }
            _shortageCount.value = shortage

            //over
            val tagsNotInStock = tagMasterRepository.getTagsByLocationAndStock(
                locationId = currentLocationId,
                isInStock = false
            )
            val over = tagsNotInStock.count { it.epc in scannedTags }
            _overCount.value = over

            //ok
            val ok = tagsInStock.count { it.epc in scannedTags }
            _okCount.value = ok
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