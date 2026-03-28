package com.example.sol_denka_stockmanagement.presentation.reader

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.domain.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val readerController: ReaderController,
    private val connectionObserver: NetworkConnectionObserver
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val readerInfo = readerController.readerInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReaderInfoModel()
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val connectionState = readerController.connectionState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConnectionState.DISCONNECTED
    )

    val isNetworkConnected = connectionObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )
}