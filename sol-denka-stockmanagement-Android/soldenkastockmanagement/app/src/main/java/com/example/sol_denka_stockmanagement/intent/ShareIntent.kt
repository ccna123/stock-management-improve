package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.constant.Tab


sealed class ShareIntent {
    data class ChangeTab(val tab: Tab): ShareIntent()
    data object ToggleDialog: ShareIntent()
    data class ToggleDropDown(val showDropDown: Boolean): ShareIntent()
}