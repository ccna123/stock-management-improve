package com.example.sol_denka_stockmanagement.screen.inventory.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.repository.InventoryItemMasterRepository
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class InventoryScanViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val inventoryItemMasterRepository: InventoryItemMasterRepository
) : ViewModel() {

}