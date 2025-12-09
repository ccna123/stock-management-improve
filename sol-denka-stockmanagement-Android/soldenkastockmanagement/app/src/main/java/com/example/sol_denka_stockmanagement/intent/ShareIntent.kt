package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.Tab


sealed interface ShareIntent {
    data class ChangeTab(val tab: Tab) : ShareIntent
    data object ToggleDialog : ShareIntent
    data object ToggleClearTagConfirmDialog : ShareIntent
    data object ToggleRadioPowerChangeDialog : ShareIntent
    data class ToggleDropDown(val showDropDown: Boolean) : ShareIntent
    data class ToggleSelectionMode(val selectionMode: Boolean) : ShareIntent
    data class ToggleFoundTag(val tag: String) : ShareIntent
    data object ClearFoundTag : ShareIntent
    data object Prev : ShareIntent
    data class Next(val lastItemIndex: Int) : ShareIntent
    data class ToggleNetworkDialog(val doesOpenDialog: Boolean) : ShareIntent
    data class ToggleTimePicker(val showTimePicker: Boolean) : ShareIntent
    data class ToggleDatePicker(val showDatePicker: Boolean) : ShareIntent
    data class ChangePerTagProcessMethod(val tag: String, val method: String) : ShareIntent
    data class ShowModalProcessMethod(val showBottomSheet: Boolean) : ShareIntent
    data object ResetState : ShareIntent
    data class ChangeTabInReceivingScreen(val tab: String) : ShareIntent
    data object ResetDetailIndex: ShareIntent

    data class SaveScanResult<T : ICsvExport>(
        val taskCode: CsvTaskType,
        val direction: CsvHistoryDirection,
        val data: List<T>
    ) : ShareIntent

    data class ShowErrorDialog(val message: String, val onOk: (() -> Unit)? = null) : ShareIntent
    data class ShowConfirmDialog(val message: String, val onOk: (() -> Unit)?, val onCancel: (() -> Unit)? = null) : ShareIntent
    data object HiddenDialog: ShareIntent
}