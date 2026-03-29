package com.example.sol_denka_stockmanagement.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.domain.usecase.location.SaveLocationChangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LocationChangeViewModel @Inject constructor(
    private val saveLocationChangeUseCase: SaveLocationChangeUseCase,
    private val saveCsvUseCase: SaveCsvUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationChangeUiState())
    val uiState: StateFlow<LocationChangeUiState> = _uiState.asStateFlow()

    private var lastExecuteIntent: LocationChangeIntent.Execute? = null

    fun onIntent(intent: LocationChangeIntent) {
        when (intent) {
            is LocationChangeIntent.Execute -> {
                lastExecuteIntent = intent
                execute(intent)
            }
            is LocationChangeIntent.Retry -> {
                lastExecuteIntent?.let { execute(it) }
            }
        }
    }

    private fun execute(
        intent: LocationChangeIntent.Execute
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Save to DB
            val dbResult = saveLocationChangeUseCase.saveToDb(
                memo = intent.memo,
                locationId = intent.locationId,
                scannedAt = intent.scannedAt,
                executedAt = intent.executedAt,
                sourceEventIdByTagId = intent.sourceEventIdByTagId,
                rfidTagList = intent.rfidTagList
            )

            if (dbResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        event = LocationChangeEvent.SaveDbFailed
                    )
                }
                return@launch
            }

            // 2. Generate CSV
            val csvData = saveLocationChangeUseCase.generateCsvData(
                memo = intent.memo,
                locationId = intent.locationId,
                scannedAt = intent.scannedAt,
                executedAt = intent.executedAt,
                sourceEventIdByTagId = intent.sourceEventIdByTagId,
                rfidTagList = intent.rfidTagList
            )

            // 3. Save CSV
            val csvResult = saveCsvUseCase(
                taskCode = CsvTaskType.LOCATION_CHANGE,
                direction = CsvHistoryDirection.EXPORT,
                data = csvData,
                onProgress = { _uiState.update { s -> s.copy(progress = it) } }
            )

            val event = if (csvResult.isSuccess)
                LocationChangeEvent.SaveCsvSuccess
            else
                LocationChangeEvent.SaveCsvFailed

            _uiState.update { it.copy(isLoading = false, event = event) }
        }
    }

}