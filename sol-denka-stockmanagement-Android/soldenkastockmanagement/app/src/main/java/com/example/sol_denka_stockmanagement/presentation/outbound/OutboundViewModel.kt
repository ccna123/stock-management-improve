package com.example.sol_denka_stockmanagement.presentation.outbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.domain.usecase.outbound.SaveOutboundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OutboundViewModel @Inject constructor(
    private val saveOutboundUseCase:  SaveOutboundUseCase,
    private val saveCsvUseCase: SaveCsvUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OutboundUiState())
    val uiState: StateFlow<OutboundUiState> = _uiState.asStateFlow()

    private var lastExecuteIntent: OutboundIntent.Execute? = null

    fun onIntent(intent: OutboundIntent) {
        when (intent) {
            is OutboundIntent.Execute -> {
                lastExecuteIntent = intent
                execute(intent)
            }
            is OutboundIntent.Retry -> lastExecuteIntent?.let { execute(it) }
            is OutboundIntent.MemoChanged -> _uiState.update { it.copy(memo = intent.value) }
            is OutboundIntent.ProcessedAtDateChanged -> _uiState.update { it.copy(processedAtDate = intent.value) }
            is OutboundIntent.ProcessedAtTimeChanged -> _uiState.update { it.copy(processedAtTime = intent.value) }
            is OutboundIntent.ToggleDatePicker -> _uiState.update { it.copy(showDatePicker = intent.show) }
            is OutboundIntent.ToggleTimePicker -> _uiState.update { it.copy(showTimePicker = intent.show) }
            is OutboundIntent.EventConsumed -> _uiState.update { it.copy(event = null) }
        }
    }

    private fun execute(intent: OutboundIntent.Execute) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Save to DB
            val dbResult = saveOutboundUseCase.saveToDb(
                memo = intent.memo,
                processedAt = intent.processedAt,
                registeredAt = intent.registeredAt,
                executedAt = intent.executedAt,
                sourceEventIdByTagId = intent.sourceEventIdByTagId,
                rfidTagList = intent.rfidTagList
            )

            if (dbResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, event = OutboundEvent.SaveDbFailed) }
                return@launch
            }

            // 2. Generate CSV
            val csvData = saveOutboundUseCase.generateCsvData(
                memo = intent.memo,
                processedAt = intent.processedAt,
                registeredAt = intent.registeredAt,
                sourceEventIdByTagId = intent.sourceEventIdByTagId,
                rfidTagList = intent.rfidTagList
            )

            // 3. Save CSV
            val csvResult = saveCsvUseCase(
                taskCode = CsvTaskType.OUT,
                direction = CsvHistoryDirection.EXPORT,
                data = csvData,
                onProgress = { _uiState.update { s -> s.copy(progress = it) } }
            )

            val event = if (csvResult.isSuccess)
                OutboundEvent.SaveCsvSuccess
            else
                OutboundEvent.SaveCsvFailed

            _uiState.update { it.copy(isLoading = false, event = event) }
        }
    }
}