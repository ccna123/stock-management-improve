package com.example.sol_denka_stockmanagement.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val readerController: ReaderController,
) : ViewModel() {

    val scannedTags = readerController.scannedTags

    suspend fun startInventory() {
        readerController.startInventory()
    }

    suspend fun stopInventory() {
        readerController.stopInventory()
    }

    fun clearScannedTag() = readerController.clearScannedTag()

    fun setEnableScan(enabled: Boolean, screen: Screen = Screen.Inbound) =
        readerController.setScanEnabled(enabled, screen = screen)

}