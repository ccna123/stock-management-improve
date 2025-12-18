package com.example.sol_denka_stockmanagement.screen.csv

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.json.JsonFileManager
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.CsvIntent
import com.example.sol_denka_stockmanagement.model.csv.CsvFileInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CsvViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val helper: CsvHelper,
) : ViewModel() {

    private val _csvFiles = MutableStateFlow<List<CsvFileInfoModel>>(emptyList())
    val csvFiles: StateFlow<List<CsvFileInfoModel>> = _csvFiles.asStateFlow()

    init {
        viewModelScope.launch {
            helper.createAppFolders(context)
        }
    }

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

    private val _importFileSelectedIndex = MutableStateFlow(-1)
    val importFileSelectedIndex: StateFlow<Int> = _importFileSelectedIndex.asStateFlow()

    private val _importFileSelectedName = MutableStateFlow("")
    val importFileSelectedName: StateFlow<String> = _importFileSelectedName.asStateFlow()

    private val _csvType = MutableStateFlow("")
    val csvType: StateFlow<String> = _csvType.asStateFlow()

    fun onCsvIntent(intent: CsvIntent) {
        when (intent) {
            is CsvIntent.ToggleFileSelect -> {
                _importFileSelectedIndex.value = intent.fileIndex
                _importFileSelectedName.value = intent.fileName

                _importResultStatus.value = null
                _importProgress.value = 0f
                _showProgress.value = false
            }

            is CsvIntent.ResetFileSelect -> {
                _importFileSelectedIndex.value = -1
                _importFileSelectedName.value = ""
            }

            is CsvIntent.SelectCsvType -> _csvType.value = intent.csvType
            CsvIntent.ResetImportStatus -> {
                _importResultStatus.value = null
                _importFileSelectedName.value = ""
                _importFileSelectedIndex.value = -1
            }

            is CsvIntent.ToggleProgressVisibility -> _showProgress.value = intent.show
            CsvIntent.ClearCsvFileList -> _csvFiles.value = emptyList()
            CsvIntent.FetchCsvFiles -> fetchCsvFiles()

            CsvIntent.ResetCsvType -> _csvType.value = ""
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

    fun exportAllFilesIndividually(context: Context, isInventoryResult: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val files = _csvFiles.value.toMutableList()
            helper.exportAllFilesIndividually(
                context = context,
                files = files,
                isInventoryResult = isInventoryResult,
                onProgressUpdate = { index, progress ->
                    updateFileProgress(index = index, progress = progress)
                    _isExporting.value = true
                },
                onFileComplete = { index ->
                    markFileCompleted(index = index)
                    _isExporting.value = false
                },
                onFileError = { index ->
                    markFileError(index)
                }
            )
            Log.i("TSS", "🎉 All files exported successfully")
        }
    }

    suspend fun downloadCsvFromSftp(context: Context): ProcessResult {
        return withContext(Dispatchers.IO) {
            val config = JsonFileManager.loadSftpConfig(context)
            val result = helper.downloadCsvFromSftp(
                context = context,
                host = config.host,
                username = config.userName,
                csvType = _csvType.value,
                onProgress = { progress ->
                    _isImporting.value = true
                    _importProgress.value = progress
                },
                onComplete = {
                    viewModelScope.launch {
                        delay(2000)
                        _isImporting.value = false
                        _importProgress.value = 1f
                    }
                }
            )
            if (result is ProcessResult.Success) {
                fetchCsvFiles()
            } else if (result is ProcessResult.Failure) {
                val msg = result.rawMessage
                    ?: MessageMapper.toMessage(result.statusCode)
                showProcessResultDialog(msg)
            }
            result // ✅ return whatever helper produced
        }
    }

    suspend fun importMaster() {
        return withContext(Dispatchers.IO) {
            _isImporting.value = true
            _importProgress.value = 0f

            val result = helper.import(
                csvType = _csvType.value,
                fileName = importFileSelectedName.value,
                onProgress = { p ->
                    _importProgress.value = p
                }
            )

            _isImporting.value = false
            _importResultStatus.value = result

            if (result is ProcessResult.Failure) {
                val msg = result.rawMessage
                    ?: MessageMapper.toMessage(result.statusCode)
                showProcessResultDialog(msg)
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