package com.example.sol_denka_stockmanagement.presentation.inventory.scan

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InventoryScanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryScanUiState())
    val uiState: StateFlow<InventoryScanUiState> = _uiState.asStateFlow()

    fun onIntent(intent: InventoryScanIntent) {
        when (intent) {
            is InventoryScanIntent.ChangeTab ->
                _uiState.update { it.copy(tab = intent.tab) }
            is InventoryScanIntent.ToggleSelectionMode ->
                _uiState.update { it.copy(isSelectionMode = intent.enabled) }
            is InventoryScanIntent.ToggleDropDown ->
                _uiState.update { it.copy(showDropDown = intent.show) }
        }
    }
}