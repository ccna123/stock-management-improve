package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.Tab


sealed interface ShareIntent {
    data class ChangeTab(val tab: Tab): ShareIntent
    data object ToggleDialog: ShareIntent
    data object ToggleClearTagConfirmDialog: ShareIntent
    data object ToggleRadioPowerChangeDialog: ShareIntent
    data class ToggleDropDown(val showDropDown: Boolean): ShareIntent
    data class ToggleSelectionMode(val selectionMode: Boolean): ShareIntent
    data class ToggleTagSelection(val item: String): ShareIntent
    data class ToggleTagSelection1(val tag: String, val totalTag: Int): ShareIntent
    data object ClearTagSelectionList: ShareIntent
    data class ToggleFoundTag(val tag: String): ShareIntent
    data object ClearFoundTag: ShareIntent
    data object Prev: ShareIntent
    data object Next: ShareIntent
    data class ToggleSelectionAll(val tagList: Set<String>): ShareIntent
    data class ToggleNetworkDialog(val doesOpenDialog: Boolean): ShareIntent
    data class ToggleTimePicker(val showTimePicker: Boolean): ShareIntent
    data class ChangePerTagHandlingMethod(val tag: String, val method: String): ShareIntent
    data class ShowModalHandlingMethod(val showBottomSheet: Boolean): ShareIntent
    data object ResetState: ShareIntent
    data class ChangeTabInReceivingScreen(val tab: String): ShareIntent

    data class SaveScanResult<T: ICsvExport>(val data: List<T>): ShareIntent
    data class UpdateSelectionStatus(val selectedCount: Int, val allSelected: Boolean) : ShareIntent
}