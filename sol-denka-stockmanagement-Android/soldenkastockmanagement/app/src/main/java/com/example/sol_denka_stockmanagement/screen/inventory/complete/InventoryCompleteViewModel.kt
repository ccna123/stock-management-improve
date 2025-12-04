package com.example.sol_denka_stockmanagement.screen.inventory.complete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
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
            val scannedTags = rfidTagList.map { it.epc }.toSet()

            val tagsInStock = tagMasterRepository.getTagsByLocationAndStock(
                locationId = currentLocationId,
                isInStock = true
            ).map { it.epc }.toList()


            val newList = rfidTagList.map { tag ->
                val wrongLocation = isWrongLocation(tag.epc, currentLocationId)
                val shortage = tag.epc in tagsInStock && tag.epc !in scannedTags
                val over = tag.epc !in tagsInStock && tag.epc in scannedTags
                val ok = !wrongLocation && !shortage && !over

                val resultType = when {
                    wrongLocation -> InventoryResultType.FOUND_WRONG_LOCATION
                    shortage -> InventoryResultType.NOT_FOUND
                    over -> InventoryResultType.FOUND_OVER_STOCK
                    ok -> InventoryResultType.FOUND_OK
                    else -> InventoryResultType.UNKNOWN
                }
                tag.copy(newFields = tag.newFields.copy(inventoryResultType = resultType))
            }
            Log.e("TSS", "computeResult: ${newList.map { it.newFields.inventoryResultType }}", )
            _wrongLocationCount.value = newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_WRONG_LOCATION }
            _shortageCount.value = newList.count { it.newFields.inventoryResultType == InventoryResultType.NOT_FOUND }
            _overCount.value = newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_OVER_STOCK }
            _okCount.value = newList.count { it.newFields.inventoryResultType == InventoryResultType.FOUND_OK }
        }
    }


    private suspend fun isWrongLocation(epc: String, selectedLocationId: Int): Boolean {
        val tagLocationId = tagMasterRepository.getLocationIdByTag(epc)
        return tagLocationId == null || selectedLocationId != tagLocationId
    }

    private suspend fun computeWrongLocationCount(
        rfidTagList: List<TagMasterModel>,
        selectedLocationId: Int
    ): Int {
        return rfidTagList.count { tag ->
            isWrongLocation(tag.epc, selectedLocationId)
        }
    }


    private suspend fun computeShortage(
        currentLocationId: Int,
        scannedTags: Set<String>
    ): Int {
        val tagsInStock = tagMasterRepository.getTagsByLocationAndStock(
            locationId = currentLocationId,
            isInStock = true
        )

        return tagsInStock.count { it.epc !in scannedTags }
    }

    private suspend fun computeOverload(
        currentLocationId: Int,
        scannedTags: Set<String>
    ): Int {
        val tagsNotInStock = tagMasterRepository.getTagsByLocationAndStock(
            locationId = currentLocationId,
            isInStock = false
        )

        return tagsNotInStock.count { it.epc in scannedTags }
    }

    private suspend fun computeOk(
        currentLocationId: Int,
        scannedTags: Set<String>
    ): Int {
        val tagsInStock = tagMasterRepository.getTagsByLocationAndStock(
            locationId = currentLocationId,
            isInStock = true
        )
        return tagsInStock.count { it.epc in scannedTags }
    }




    private suspend fun getLocationIdByName(locationName: String): Int {
        return locationRepository.getLocationIdByName(locationName = locationName) ?: 0
    }
}