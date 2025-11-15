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

    private val _readerSettingState = MutableStateFlow(ReaderSettingState())
    val readerSettingState: StateFlow<ReaderSettingState> = _readerSettingState.asStateFlow()

    val initialReaderSettingsState = mutableStateOf(ReaderSettingState())

    init {
        viewModelScope.launch {
            var isInitialSet = false
            readerController.readerInfo.collect { info ->
                // 🔹 Update current state each time reader info changes
                _readerSettingState.update {
                    it.copy(
                        supportedChannels = info.supportedChannels,
                        enabledRfidChannel = info.supportedChannels.ifEmpty { emptyList() },
                        radioPower = info.radioPower,
                        radioPowerMw = ReaderSettingState.Companion.calculateMwFromDbm(info.radioPower),
                        radioPowerSliderPosition = info.radioPower,
                        buzzerVolume = when (info.buzzerVolume) {
                            FakeBeeperVolume.QUIET_BEEP -> {
                                FakeBeeperVolume.QUIET_BEEP
                            }

                            FakeBeeperVolume.LOW_BEEP -> {
                                FakeBeeperVolume.LOW_BEEP
                            }

                            FakeBeeperVolume.MEDIUM_BEEP -> {
                                FakeBeeperVolume.MEDIUM_BEEP
                            }

                            FakeBeeperVolume.HIGH_BEEP -> {
                                FakeBeeperVolume.HIGH_BEEP
                            }
                        },
                        tagPopulation = info.tagPopulation,
                        rfidSession = info.rfidSession,
                        tagAccessFlag = info.tagAccessFlag
                    )
                }

                // ✅ Only set the initial snapshot *after* the first valid info arrives
                if (!isInitialSet && info.supportedChannels.isNotEmpty()) {
                    initialReaderSettingsState.value = _readerSettingState.value
                    isInitialSet = true
                }
            }
        }
    }


    fun onIntent(intent: ReaderSettingIntent) {
        when (intent) {
            is ReaderSettingIntent.ChangeRadioPower -> {
                _readerSettingState.update {
                    it.copy(
                        radioPower = intent.newValue,
                        radioPowerMw = ReaderSettingState.Companion.calculateMwFromDbm(intent.newValue)
                    )
                }
            }

            is ReaderSettingIntent.ChangeSession -> {
                _readerSettingState.update { it.copy(rfidSession = intent.newValue) }
            }

            is ReaderSettingIntent.ChangeTagAccessFlag -> {
                _readerSettingState.update { it.copy(tagAccessFlag = intent.newValue) }
            }

            is ReaderSettingIntent.ChangeTagPopulation -> {
                _readerSettingState.update { it.copy(tagPopulation = intent.newValue) }
            }

            is ReaderSettingIntent.ChangeUsedChannel -> {
                _readerSettingState.update { it.copy(enabledRfidChannel = intent.newValue) }
            }

            is ReaderSettingIntent.ChangeVolume -> {
                _readerSettingState.update { it.copy(buzzerVolume = intent.newValue) }
            }

            is ReaderSettingIntent.ChangeRadioPowerSliderPosition -> {
                _readerSettingState.update { it.copy(radioPowerSliderPosition = intent.newValue) }
            }
        }
    }

    fun isSettingChangedWithoutApply(): Boolean {
        return initialReaderSettingsState.value != _readerSettingState.value
    }

    fun resetToInitialSetting() {
        _readerSettingState.value = initialReaderSettingsState.value
    }

    fun setRadioPower(radioPower: Int) {
        readerController.setRadioPower(radioPower)
    }

    fun applyReaderSetting(): Boolean {
        val sessionResult = readerController.setSession(_readerSettingState.value.rfidSession)
        val tagAccessFlagResult =
            readerController.setTagAccessFlag(_readerSettingState.value.tagAccessFlag)
        val radioPowerResult =
            readerController.setRadioPower(_readerSettingState.value.radioPower)
        val buzzerVolumeResult =
            readerController.setBuzzerVolume(
                when (_readerSettingState.value.buzzerVolume) {
                    FakeBeeperVolume.QUIET_BEEP -> {
                        FakeBeeperVolume.QUIET_BEEP
                    }

                    FakeBeeperVolume.LOW_BEEP -> {
                        FakeBeeperVolume.LOW_BEEP
                    }

                    FakeBeeperVolume.MEDIUM_BEEP -> {
                        FakeBeeperVolume.MEDIUM_BEEP
                    }

                    FakeBeeperVolume.HIGH_BEEP -> {
                        FakeBeeperVolume.HIGH_BEEP
                    }
                }
            )
        val channelResult =
            readerController.setChannel(_readerSettingState.value.enabledRfidChannel)
        val tagPopulationResult =
            readerController.setTagPopulation(_readerSettingState.value.tagPopulation)
        if (sessionResult && tagAccessFlagResult && radioPowerResult && buzzerVolumeResult && channelResult && tagPopulationResult) {
            readerController.emitUpdatedInfoFromFake()
            initialReaderSettingsState.value = _readerSettingState.value
            return true
        } else {
            resetToInitialSetting()
            return false
        }
    }
}