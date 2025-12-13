package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.Tab

data class GeneralState(
    // Input fields
    val tab: Tab = Tab.Left,
    val isSelectionMode: Boolean = false,
    val foundTags: List<String> = emptyList(),
    val currentIndex: Int = 0,
    val currentItem: String? = null,
    val targetScreenId: Screen? = null,
    var showDropDown: Boolean = false,
    var showNetworkDialog: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    var isAllSelected: Boolean = false,
    val selectedChipIndex: Int = 0,
    val inboundInputFieldDateTime: String = ""
)
