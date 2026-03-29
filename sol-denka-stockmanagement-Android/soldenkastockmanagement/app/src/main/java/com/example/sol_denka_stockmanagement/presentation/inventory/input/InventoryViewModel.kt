package com.example.sol_denka_stockmanagement.presentation.inventory.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val locationRepo: ILocationMasterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val locationMaster = locationRepo.get()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onIntent(intent: InventoryIntent) {
        when (intent) {
            is InventoryIntent.LocationChanged -> _uiState.update { it.copy(location = intent.location, locationExpanded = false) }
            is InventoryIntent.ToggleLocationExpanded -> _uiState.update { it.copy(locationExpanded = !it.locationExpanded) }
            is InventoryIntent.MemoChanged -> _uiState.update { it.copy(memo = intent.value) }
        }
    }
}