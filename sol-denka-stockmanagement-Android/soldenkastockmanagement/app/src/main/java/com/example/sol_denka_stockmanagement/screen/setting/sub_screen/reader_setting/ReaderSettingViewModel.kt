package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.state.ReaderSettingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ReaderSettingViewModel @Inject constructor(
    private val readerController: ReaderController
) : ViewModel() {


}