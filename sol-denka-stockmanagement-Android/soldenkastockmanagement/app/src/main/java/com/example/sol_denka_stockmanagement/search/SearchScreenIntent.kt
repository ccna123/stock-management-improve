package com.example.sol_denka_stockmanagement.search

import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel


sealed class SearchScreenIntent{
    data class ToggleFoundTag(val tag: InventoryItemMasterModel): SearchScreenIntent()
    data object ClearFoundTag: SearchScreenIntent()
}

