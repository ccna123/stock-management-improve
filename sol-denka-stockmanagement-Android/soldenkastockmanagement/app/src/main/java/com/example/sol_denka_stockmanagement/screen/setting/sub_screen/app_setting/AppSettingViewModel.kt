package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.helper.UsbEvent
import com.example.sol_denka_stockmanagement.helper.UsbState
import com.example.sol_denka_stockmanagement.intent.AppSettingIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingViewModel @Inject constructor(
    private val connectionObserver: NetworkConnectionObserver
//    private val fileSettingStorage: JsonFileSettingStorage<AppSettingModel>,
//    @ApplicationContext private val context: Context,
//    private val helper: AppSettingHelper
) : ViewModel() {

    private val _appSettingState = MutableStateFlow(AppSettingState())
    val appSettingState: StateFlow<AppSettingState> = _appSettingState.asStateFlow()
//
    val initialAppSettingsState = mutableStateOf(_appSettingState.value)

    private val _usbState = MutableStateFlow(UsbState(connected = false, mtp = false))
    val usbState = _usbState.asStateFlow()


    val isNetworkConnected = connectionObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false // Compute initial value synchronously
    )

    init {
        viewModelScope.launch {
            UsbEvent.usbEvents.collect { usbState ->
                _usbState.value = usbState
            }
        }
    }
//
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("UTC"))
//
//    init {
//        loadSettingFromFile()
//    }
//
    fun handle(intent: AppSettingIntent) {
        when (intent) {
            is AppSettingIntent.ChangeFileTransferMethod -> _appSettingState.update { it.copy(fileTransferMethod = intent.method) }
        }
    }
//
//    fun updateAppSettingState(transform: AppSettingState.() -> AppSettingState) {
//        _appSettingState.value = _appSettingState.value.transform()
//    }
//
//    fun setAutoConnect(isAutoConnectSwitchOn: Boolean, selectedDeviceAddress: String?) {
//        _appSettingState.update {
//            it.copy(
//                autoConnectChecked = isAutoConnectSwitchOn,
//                autoConnectDeviceAddress = selectedDeviceAddress ?: ""
//            )
//        }
//    }
//
//    fun hasAppSettingChangeWithoutSave(): Boolean {
//        return helper.hasAppSettingChangeWithoutSave(
//            currentState = _appSettingState.value,
//            initialState = initialAppSettingsState.value
//        )
//    }
//
//    fun applyAppSettingChanges(): Boolean {
//        val saveSuccess = helper.applyAppSettingChanges(
//            context = context,
//            fileSettingStorage = fileSettingStorage,
//            newState = _appSettingState.value,
//        )
//        if (saveSuccess) {
//            initialAppSettingsState.value = _appSettingState.value.copy()
//        }
//        return saveSuccess
//    }
//
//    fun loadSettingFromFile() {
//        helper.loadSettingFromFile(
//            context = context,
//            fileSettingStorage = fileSettingStorage,
//        )?.let { loaded ->
//            _appSettingState.value = loaded
//            initialAppSettingsState.value = loaded.copy()
//        }
//    }
//    fun deleteScanTask() {
//        viewModelScope.launch {
//            val deleteResult = helper.deleteScanTask(
//                fromDate = _appSettingState.value.scanDataFrom,
//                toDate = _appSettingState.value.scanDataTo,
//                formatter = formatter
//            )
//            _appSettingState.update { it.copy(isDeleteSuccess = deleteResult) }
//        }
//    }
}