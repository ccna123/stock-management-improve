package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import javax.inject.Inject

class AppSettingHelper @Inject constructor() {
    fun hasAppSettingChangeWithoutSave(
        initialState: AppSettingState,
        currentState: AppSettingState
    ): Boolean {
        return initialState.autoConnectChecked != currentState.autoConnectChecked ||
                initialState.isRfidVolumeOn != currentState.isRfidVolumeOn ||
                initialState.selectedTab != currentState.selectedTab
    }

//    fun applyAppSettingChanges(
//        context: Context,
//        fileSettingStorage: JsonFileSettingStorage<AppSettingModel>,
//        newState: AppSettingState
//    ): Boolean {
//        return try {
//            fileSettingStorage.save(
//                context,
//                AppSettingModel(
//                    autoConnectChecked = newState.autoConnectChecked,
//                    isRfidVolumeOn = newState.isRfidVolumeOn,
//                    selectedTab = newState.selectedTab,
//                    scanDataFrom = newState.scanDataFrom,
//                    scanDataTo = newState.scanDataTo
//                )
//            )
//        } catch (_: Exception) {
//            false
//        }
//    }

//    fun loadSettingFromFile(
//        context: Context,
//        fileSettingStorage: JsonFileSettingStorage<AppSettingModel>
//    ): AppSettingState? {
//        return try {
//            val model = fileSettingStorage.load(context)
//            AppSettingState(
//                autoConnectChecked = model.autoConnectChecked,
//                isRfidVolumeOn = model.isRfidVolumeOn,
//                selectedTab = model.selectedTab,
//                scanDataFrom = model.scanDataFrom,
//                scanDataTo = model.scanDataTo
//            )
//        } catch (e: Exception) {
//            Log.e("TSS", "Failed to load app state: ${e.message}")
//            null
//        }
//    }

}
