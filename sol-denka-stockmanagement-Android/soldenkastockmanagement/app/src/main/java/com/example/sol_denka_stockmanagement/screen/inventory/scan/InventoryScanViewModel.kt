package com.example.sol_denka_stockmanagement.screen.inventory.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.intent.InventoryScanIntent
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class InventoryScanViewModel @Inject constructor() : ViewModel() {


    var isSelectionMode = mutableStateOf(false)
        private set

    var selectedTags = mutableStateOf<List<InventoryItemMasterModel>>(emptyList())
        private set


}