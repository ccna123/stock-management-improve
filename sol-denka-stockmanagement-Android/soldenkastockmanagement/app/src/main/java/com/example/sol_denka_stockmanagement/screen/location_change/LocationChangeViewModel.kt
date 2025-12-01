package com.example.sol_denka_stockmanagement.screen.location_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanResult
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val tagMasterRepository: TagMasterRepository
) : ViewModel() {
    private val _locationChangePreview =
        MutableStateFlow<List<LocationChangeScanResult>>(emptyList())
    val locationChangePreview = _locationChangePreview.asStateFlow()

    fun getTagDetailForLocationChange(selectedTags: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailList = tagMasterRepository.getTagDetailForLocationChange(selectedTags)
            _locationChangePreview.value = detailList
        }
    }
}