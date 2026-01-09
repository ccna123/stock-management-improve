package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.constant.DialogType
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
    data class ToggleTimePicker(val showTimePicker: Boolean, val field: String = "") : ShareIntent
    data class ToggleDatePicker(val showDatePicker: Boolean, val field: String = "") : ShareIntent
    data class ChangePerTagProcessMethod(val tag: String, val method: String, val isChecked: Boolean) : ShareIntent
    data class ShowModalProcessMethod(val showBottomSheet: Boolean) : ShareIntent
    data object ResetState : ShareIntent
    data object ResetDetailIndex: ShareIntent
    data class SelectChipIndex(val index: Int) : ShareIntent
    data class FindItemNameByKeyWord(val keyword: String, val categoryName: String): ShareIntent

    data class ShowDialog(val message: String, val type: DialogType) : ShareIntent
    data object HiddenDialog: ShareIntent
    data class MarkOutboundProcessError(val epcs: List<String>) : ShareIntent
    data object ClearOutboundProcessError : ShareIntent
}