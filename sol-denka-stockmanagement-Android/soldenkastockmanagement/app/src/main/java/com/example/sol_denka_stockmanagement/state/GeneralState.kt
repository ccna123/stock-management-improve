package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel

data class GeneralState(
    // Input fields
    val tab: Tab = Tab.Left,
    val isSelectionMode: Boolean = false,
    var selectedTags: List<InventoryItemMasterModel> = emptyList(),
    var selectedTags1: List<String> = emptyList(),
    var foundTags: List<InventoryItemMasterModel> = emptyList(),
    val currentIndex: Int = 0,
    val currentItem: String? = null,
    val targetScreenId: Screen? = null,
    var showAppDialog: Boolean = false,
    var showDropDown: Boolean = false,
    var showNetworkDialog: Boolean = false,
    var isAllSelected: Boolean = false
)
