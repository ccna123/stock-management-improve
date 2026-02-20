package com.example.sol_denka_stockmanagement.screen.csv

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.exception.AppException
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.CsvIntent
import com.example.sol_denka_stockmanagement.model.csv.CsvFileInfoModel
import com.example.sol_denka_stockmanagement.model.session.SessionModel
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
) : ViewModel() {

    private val _csvFiles = MutableStateFlow<List<CsvFileInfoModel>>(emptyList())
    val csvFiles: StateFlow<List<CsvFileInfoModel>> = _csvFiles.asStateFlow()

    private val _exportFiles = MutableStateFlow<List<SessionModel>>(emptyList())
    val exportFiles: StateFlow<List<SessionModel>> = _exportFiles.asStateFlow()

    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    private val _isImporting = MutableStateFlow(false)
    val isImporting = _isImporting.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting = _isExporting.asStateFlow()

    private val _importProgress = MutableStateFlow(0f)
    val importProgress: StateFlow<Float> = _importProgress.asStateFlow()

    private val _processResultMessage = MutableStateFlow<String?>(null)
    val processResultMessage = _processResultMessage.asStateFlow()

    private val _showProcessResultDialog = MutableStateFlow(false)
    val showProcessResultDialog = _showProcessResultDialog.asStateFlow()

    private val _importResultStatus = MutableStateFlow<ProcessResult?>(null)
    val importResultStatus = _importResultStatus.asStateFlow()

    private val _exportResultStatus = MutableStateFlow<ProcessResult?>(null)
    val exportResultStatus = _exportResultStatus.asStateFlow()

    private val _importFileSelectedIndex = MutableStateFlow(-1)
    val importFileSelectedIndex: StateFlow<Int> = _importFileSelectedIndex.asStateFlow()

    private val _importFileSelectedName = MutableStateFlow("")
    val importFileSelectedName: StateFlow<String> = _importFileSelectedName.asStateFlow()

    private val _exportFileSelectedIndex = MutableStateFlow(-1)
    val exportFileSelectedIndex: StateFlow<Int> = _exportFileSelectedIndex.asStateFlow()

    private val _exportFileSessionId = MutableStateFlow(0)
    val exportFileSessionId: StateFlow<Int> = _exportFileSessionId.asStateFlow()

    private val _csvType = MutableStateFlow("")
    val csvType: StateFlow<String> = _csvType.asStateFlow()

    val csvModels = mutableListOf<ICsvExport>()

    fun onCsvIntent(intent: CsvIntent) {
        when (intent) {
            is CsvIntent.ToggleFileSelect -> {
                if (intent.type == CsvHistoryDirection.IMPORT.displayName) {
                    _importFileSelectedIndex.value = intent.fileIndex
                    _importFileSelectedName.value = intent.fileName

                    _importResultStatus.value = null
                    _importProgress.value = 0f
                    _showProgress.value = false
                } else {
                    _exportFileSelectedIndex.value = intent.fileIndex
                    _exportFileSessionId.value = intent.fileSessionId
                    _showProgress.value = false
                }
            }

            is CsvIntent.ResetFileSelect -> {
                _importFileSelectedIndex.value = -1
                _importFileSelectedName.value = ""

                _exportFileSelectedIndex.value = -1
                _exportFileSessionId.value = 0
            }

            is CsvIntent.SelectCsvType -> _csvType.value = intent.csvType
            CsvIntent.ResetFileSelectedStatus -> {
                _importResultStatus.value = null
                _importFileSelectedName.value = ""
                _importFileSelectedIndex.value = -1

                _exportFileSessionId.value = 0
                _exportFileSelectedIndex.value = -1
            }

            is CsvIntent.ToggleProgressVisibility -> _showProgress.value = intent.show
            CsvIntent.ClearCsvFileList -> {
                _csvFiles.value = emptyList()
                _exportFiles.value = emptyList()
            }
            CsvIntent.FetchCsvFiles -> fetchCsvFiles()

            CsvIntent.ResetCsvType -> _csvType.value = ""
            CsvIntent.FetchExportCsvFiles -> listExportFileName()
        }
    }

    private fun fetchCsvFiles() {
        viewModelScope.launch {
            val result = helper.listCsvFiles(
                csvType = _csvType.value
            )
            _csvFiles.value = result
        }
    }

    suspend fun getEventDataBySessionId(
        sessionId: Int,
        type: String
    ): List<ICsvExport> {
        return try {
            helper.getEventDataBySessionId(
                sessionId = sessionId,
                type = type,
                deviceId = Build.ID
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun exportToCsvFile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _exportResultStatus.value = ProcessResult.Success()
                showProcessResultDialog(
                    MessageMapper.toMessage(StatusCode.EXPORT_OK)
                )
                Log.i("TSS", "🎉 All files exported successfully")
            } catch (e: Exception) {
                Log.e("TSS", "exportToCsvFile: $e")
                showProcessResultDialog(
                    MessageMapper.toMessage(StatusCode.EXPORT_FAILED)
                )
                _exportResultStatus.value =
                    ProcessResult.Failure(statusCode = StatusCode.EXPORT_FAILED)
            }
        }
    }

    fun listExportFileName() {
        viewModelScope.launch {
            val result = helper.listExportFileName(
                csvType = _csvType.value
            )
            _exportFiles.value = result
            Log.e("TSS", "listExportFileName: ${_exportFiles.value}")
        }
    }

//    suspend fun downloadCsvFromSftp(context: Context): ProcessResult {
//        return withContext(Dispatchers.IO) {
//            val config = JsonFileManager.loadSftpConfig(context)
//            val result = helper.downloadCsvFromSftp(
//                context = context,
//                host = config.host,
//                username = config.userName,
//                csvType = _csvType.value,
//                onProgress = { progress ->
//                    _isImporting.value = true
//                    _importProgress.value = progress
//                },
//                onComplete = {
//                    viewModelScope.launch {
//                        delay(2000)
//                        _isImporting.value = false
//                        _importProgress.value = 1f
//                    }
//                }
//            )
//            if (result is ProcessResult.Success) {
//                fetchCsvFiles()
//            } else if (result is ProcessResult.Failure) {
//                val msg = result.rawMessage
//                    ?: MessageMapper.toMessage(result.statusCode)
//                showProcessResultDialog(msg)
//            }
//            result // ✅ return whatever helper produced
//        }
//    }

    fun importMaster() {
        viewModelScope.launch {
            try {
                _isImporting.value = true
                _importProgress.value = 0f

                helper.import(
                    csvType = _csvType.value,
                    fileName = importFileSelectedName.value,
                    onProgress = { _importProgress.value = it }
                )

                _isImporting.value = false
                _importResultStatus.value = ProcessResult.Success()
                showProcessResultDialog(
                    MessageMapper.toMessage(StatusCode.IMPORT_OK)
                )

            } catch (e: AppException) {
                Log.e("TSS", "importMaster error: $e ")
                val msg = e.message ?: MessageMapper.toMessage(
                    e.statusCode,
                    e.params
                )
                showProcessResultDialog(msg)
                _importResultStatus.value =
                    ProcessResult.Failure(statusCode = StatusCode.FAILED, rawMessage = msg)

            } catch (e: Exception) {
                Log.e("TSS", "importMaster error: $e ")
                showProcessResultDialog(
                    MessageMapper.toMessage(StatusCode.FAILED)

                )
                _importResultStatus.value = ProcessResult.Failure(statusCode = StatusCode.FAILED)
            } finally {
                _isImporting.value = false
            }
        }
    }

    private fun updateFileProgress(index: Int, progress: Float) {
        _csvFiles.update { current ->
            current.toMutableList().apply {
                val file = this[index]
                this[index] = file.copy(progress = progress)
            }
        }
    }

    private fun markFileCompleted(index: Int) {
        _csvFiles.update { current ->
            current.toMutableList().apply {
                val file = this[index]
                this[index] = file.copy(progress = 1f, isCompleted = true)
            }
        }
    }

    private fun markFileError(index: Int) {
        _csvFiles.update { current ->
            current.toMutableList().apply {
                val file = this[index]
                this[index] = file.copy(progress = 0f, isFailed = true)
            }
        }
    }

    fun showProcessResultDialog(message: String = "") {
        _processResultMessage.value = message
        _showProcessResultDialog.value = true
    }

    fun dismissProcessResultDialog() {
        _showProcessResultDialog.value = false
        _processResultMessage.value = null
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("TSS", "CsvViewModel: is cleared")
    }
}