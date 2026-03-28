package com.example.sol_denka_stockmanagement.presentation.csv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.exception.AppException
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.state.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CsvViewModel @Inject constructor(
    private val saveCsv: SaveCsvUseCase
) : ViewModel() {

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private val _isFileWorking = MutableStateFlow(false)
    val isFileWorking = _isFileWorking.asStateFlow()

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Hidden)
    val dialogState = _dialogState.asStateFlow()

    fun saveScanResultToCsv(
        taskCode: CsvTaskType,
        direction: CsvHistoryDirection,
        data: List<ICsvExport>
    ) {
        viewModelScope.launch {
            _isFileWorking.value = true
            _progress.value = 0f

            val result = saveCsv(
                taskCode = taskCode,
                direction = direction,
                data = data,
                onProgress = { _progress.value = it }
            )

            result.onFailure { e ->
                _dialogState.value = when (e) {
                    is AppException -> DialogState.Error(MessageMapper.toMessage(e.statusCode, e.params))
                    else -> DialogState.Error(MessageMapper.toMessage(StatusCode.FAILED))
                }
            }

            _isFileWorking.value = false
        }
    }
}