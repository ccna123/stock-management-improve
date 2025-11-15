package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.Tab

data class GeneralState(
    // Input fields
    val tab: Tab = Tab.Left,
    val isSelectionMode: Boolean = false,
    val selectedItems: Set<String> = emptySet(),
    val currentSelectedItemIndex: Int = 0,
    val currentItem: String? = null,
    val targetScreenId: Screen? = null,
    var showAppDialog: Boolean = false,
    var showDropDown: Boolean = false,
)
