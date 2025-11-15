package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSettingViewModel @Inject constructor(
//    private val fileSettingStorage: JsonFileSettingStorage<AppSettingModel>,
//    @ApplicationContext private val context: Context,
//    private val helper: AppSettingHelper
) : ViewModel() {

//    private val _appSettingState = MutableStateFlow(AppSettingState())
//    val appSettingState: StateFlow<AppSettingState> = _appSettingState.asStateFlow()
//
//    val initialAppSettingsState = mutableStateOf(_appSettingState.value)
//
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("UTC"))
//
//    init {
//        loadSettingFromFile()
//    }
//
//    fun onAppSettingIntent(intent: AppSettingIntent) {
//        when (intent) {
//            AppSettingIntent.ApplySettingChange -> applyAppSettingChanges()
//            AppSettingIntent.HasAppSettingChangedWithoutSave -> hasAppSettingChangeWithoutSave()
//            AppSettingIntent.DeleteScanTask -> deleteScanTask()
//            AppSettingIntent.LoadSettingFromFile -> loadSettingFromFile()
//            AppSettingIntent.ResetDeleteSuccess -> _appSettingState.update { it.copy(isDeleteSuccess = false) }
//            is AppSettingIntent.SetAutoConnect -> setAutoConnect(
//                intent.isAutoConnectSwitchOn,
//                intent.selectedDeviceAddress
//            )
//        }
//    }
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