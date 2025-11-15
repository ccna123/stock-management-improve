package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import com.example.sol_denka_stockmanagement.screen.scan.shipping.ShippingScanIntent


sealed interface ShareIntent {
    data class ChangeTab(val tab: Tab): ShareIntent
    data object ToggleDialog: ShareIntent
    data class ToggleDropDown(val showDropDown: Boolean): ShareIntent
    data class ToggleSelectionMode(val selectionMode: Boolean): ShareIntent
    data class ToggleTagSelection(val item: InventoryItemMasterModel): ShareIntent
    data class ToggleTagSelection1(val tag: String, val totalTag: Int): ShareIntent
    data object ClearTagSelectionList: ShareIntent
    data class ToggleFoundTag(val tag: InventoryItemMasterModel): ShareIntent
    data object ClearFoundTag: ShareIntent
    data object Prev: ShareIntent
    data object Next: ShareIntent
    data class ToggleSelectionAll(val tagList: List<String>): ShareIntent
}