package com.example.sol_denka_stockmanagement.screen.csv

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.JsonFileManager
import com.example.sol_denka_stockmanagement.helper.ProcessResult
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
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

    private val _csvState = MutableStateFlow(CsvState())
    val csvState: StateFlow<CsvState> = _csvState.asStateFlow()

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

    // Update UI state in a consistent way
    fun updateState(updates: CsvState.() -> CsvState) {
        _csvState.update { it.updates() }
    }
    suspend fun fetchCsvFiles(){
        val result =  helper.listCsvFiles(
            csvType = _csvState.value.csvType
        )
        _csvFiles.value = result
    }

    fun toggleProgressVisibility(show: Boolean){
        _showProgress.value = show
    }

    fun toggleCsvTypeExpanded() {
        updateState { copy(csvTypeExpanded = !csvTypeExpanded) }
    }

    fun resetState() {
        _csvState.value = CsvState()
    }
    fun clearCsvList(){
        _csvFiles.value = emptyList()
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
                csvType = _csvState.value.csvType,
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
                showProcessResultDialog(result.message)
            }
            result // ✅ return whatever helper produced
        }
    }

    suspend fun importMaster(){
        return withContext(Dispatchers.IO) {
            _isImporting.value = true
            _importProgress.value = 0f

            val result = helper.import(
                csvType = _csvState.value.csvType,
                onProgress = { p ->
                    _importProgress.value = p
                }
            )

            _isImporting.value = false
            _importResultStatus.value = result

            when (result) {
                is ProcessResult.Success -> {
                    showProcessResultDialog("取り込み成功しました")
                }
                is ProcessResult.Failure -> {
                    showProcessResultDialog(result.message)
                }
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
        Log.e("TSS", "CsvViewModel: is cleared")
    }
}