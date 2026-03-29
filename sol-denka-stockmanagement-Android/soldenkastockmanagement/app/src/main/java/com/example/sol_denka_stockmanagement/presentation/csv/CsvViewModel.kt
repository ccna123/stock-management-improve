package com.example.sol_denka_stockmanagement.presentation.csv

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.exception.AppException
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CsvViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val helper: CsvHelper,
    private val saveCsv: SaveCsvUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CsvUiState())
    val uiState: StateFlow<CsvUiState> = _uiState.asStateFlow()

    fun onIntent(intent: CsvIntent) {
        when (intent) {
            is CsvIntent.SelectCsvType -> _uiState.update {
                it.copy(csvType = intent.csvType, csvTypeExpanded = false)
            }
            is CsvIntent.ResetCsvType -> _uiState.update { it.copy(csvType = "") }
            is CsvIntent.ToggleCsvTypeExpanded -> _uiState.update {
                it.copy(csvTypeExpanded = !it.csvTypeExpanded)
            }
            is CsvIntent.ToggleFileSelect -> {
                if (intent.type == CsvHistoryDirection.IMPORT.displayName) {
                    _uiState.update {
                        it.copy(
                            importFileSelectedIndex = intent.fileIndex,
                            importFileSelectedName = intent.fileName,
                            importResultStatus = null,
                            importProgress = 0f,
                            showProgress = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            exportFileSelectedIndex = intent.fileIndex,
                            exportFileSessionId = intent.fileSessionId,
                            showProgress = false
                        )
                    }
                }
            }
            is CsvIntent.ResetFileSelect -> _uiState.update {
                it.copy(
                    importFileSelectedIndex = -1,
                    importFileSelectedName = "",
                    exportFileSelectedIndex = -1,
                    exportFileSessionId = 0
                )
            }
            is CsvIntent.ResetFileSelectedStatus -> _uiState.update {
                it.copy(
                    importResultStatus = null,
                    importFileSelectedName = "",
                    importFileSelectedIndex = -1,
                    exportFileSessionId = 0,
                    exportFileSelectedIndex = -1
                )
            }
            is CsvIntent.ToggleProgressVisibility -> _uiState.update {
                it.copy(showProgress = intent.show)
            }
            is CsvIntent.ClearCsvFileList -> _uiState.update {
                it.copy(csvFiles = emptyList(), exportFiles = emptyList())
            }
            is CsvIntent.FetchCsvFiles -> fetchCsvFiles()
            is CsvIntent.FetchExportCsvFiles -> fetchExportFiles()
            is CsvIntent.ImportMaster -> importMaster()
            is CsvIntent.ExportCsv -> exportCsv(intent.sessionId, intent.csvType)
            is CsvIntent.DismissProcessResultDialog -> _uiState.update {
                it.copy(showProcessResultDialog = false, processResultMessage = null)
            }
            is CsvIntent.EventConsumed -> _uiState.update { it.copy(event = null) }
        }
    }

    private fun fetchCsvFiles() {
        viewModelScope.launch {
            val result = helper.listCsvFiles(csvType = _uiState.value.csvType)
            _uiState.update { it.copy(csvFiles = result) }
        }
    }

    private fun fetchExportFiles() {
        viewModelScope.launch {
            val result = helper.listExportFileName(csvType = _uiState.value.csvType)
            _uiState.update { it.copy(exportFiles = result) }
        }
    }

    private fun importMaster() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isImporting = true, importProgress = 0f) }
                helper.import(
                    csvType = _uiState.value.csvType,
                    fileName = _uiState.value.importFileSelectedName,
                    onProgress = { progress ->
                        _uiState.update { it.copy(importProgress = progress) }
                    }
                )
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        importResultStatus = ProcessResult.Success(),
                        showProcessResultDialog = true,
                        processResultMessage = MessageMapper.toMessage(StatusCode.IMPORT_OK)
                    )
                }
            } catch (e: AppException) {
                val msg = e.message ?: MessageMapper.toMessage(e.statusCode, e.params)
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        importResultStatus = ProcessResult.Failure(statusCode = StatusCode.FAILED, rawMessage = msg),
                        showProcessResultDialog = true,
                        processResultMessage = msg
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        importResultStatus = ProcessResult.Failure(statusCode = StatusCode.FAILED),
                        showProcessResultDialog = true,
                        processResultMessage = MessageMapper.toMessage(StatusCode.FAILED)
                    )
                }
            }
        }
    }

    private fun exportCsv(sessionId: Int, csvType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(isExporting = true) }
                val data = helper.getEventDataBySessionId(
                    sessionId = sessionId,
                    type = csvType,
                    deviceId = Build.ID
                )
                val csvResult = saveCsv(
                    taskCode = CsvTaskType.OUT,
                    direction = CsvHistoryDirection.EXPORT,
                    data = data,
                    onProgress = {}
                )
                if (csvResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            exportResultStatus = ProcessResult.Success(),
                            event = CsvEvent.ExportSuccess
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            exportResultStatus = ProcessResult.Failure(statusCode = StatusCode.EXPORT_FAILED),
                            event = CsvEvent.ExportFailed
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("TSS", "exportCsv: $e")
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        exportResultStatus = ProcessResult.Failure(statusCode = StatusCode.EXPORT_FAILED),
                        event = CsvEvent.ExportFailed
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("TSS", "CsvViewModel: is cleared")
    }
}