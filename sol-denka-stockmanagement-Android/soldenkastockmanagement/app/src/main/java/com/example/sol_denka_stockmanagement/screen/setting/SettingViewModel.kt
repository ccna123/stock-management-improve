package com.example.sol_denka_stockmanagement.screen.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.intent.SettingIntent
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
class SettingViewModel @Inject constructor(
    private val readerController: ReaderController
): ViewModel() {
    private val _readerSettingState = MutableStateFlow(ReaderSettingState())
    val readerSettingState: StateFlow<ReaderSettingState> = _readerSettingState.asStateFlow()

    val initialReaderSettingsState = mutableStateOf(ReaderSettingState())

    private val _showUnsaveConfirmDialog = MutableStateFlow(false)
    val showUnsaveConfirmDialog = _showUnsaveConfirmDialog.asStateFlow()

    private val _showApplySettingConfirmDialog = MutableStateFlow(false)
    val showApplySettingConfirmDialog = _showApplySettingConfirmDialog.asStateFlow()


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
                        tagPopulation = info.tagPopulation.toString(),
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


    fun onSettingIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.ChangeRadioPower -> {
                _readerSettingState.update {
                    it.copy(
                        radioPower = intent.newValue,
                        radioPowerMw = ReaderSettingState.Companion.calculateMwFromDbm(intent.newValue)
                    )
                }
            }

            is SettingIntent.ChangeSession -> {
                _readerSettingState.update { it.copy(rfidSession = intent.newValue) }
            }

            is SettingIntent.ChangeTagAccessFlag -> {
                _readerSettingState.update { it.copy(tagAccessFlag = intent.newValue) }
            }

            is SettingIntent.ChangeTagPopulation -> {
                _readerSettingState.update { it.copy(tagPopulation = intent.newValue) }
            }

            is SettingIntent.ChangeUsedChannel -> {
                _readerSettingState.update { it.copy(enabledRfidChannel = intent.newValue) }
            }

            is SettingIntent.ChangeVolume -> {
                _readerSettingState.update { it.copy(buzzerVolume = intent.newValue) }
            }

            is SettingIntent.ChangeRadioPowerSliderPosition -> {
                _readerSettingState.update { it.copy(radioPowerSliderPosition = intent.newValue) }
            }

            is SettingIntent.ToggleUnsaveConfirmDialog -> _showUnsaveConfirmDialog.value = intent.showUnsaveConfirmDialog
            is SettingIntent.ToggleApplySettingConfirmDialog -> _showApplySettingConfirmDialog.value = intent.showApplySettingConfirmDialog
            SettingIntent.ResetToInitialSetting -> resetToInitialSetting()
        }
    }

    fun isSettingChangedWithoutApply(): Boolean {
        return initialReaderSettingsState.value != _readerSettingState.value
    }

    private fun resetToInitialSetting() {
        _readerSettingState.value = initialReaderSettingsState.value
    }


    fun setRadioPower(radioPower: Int) {
        readerController.apply {
            setRadioPower(radioPower)
            emitUpdatedInfoFromFake()
        }
        initialReaderSettingsState.value = _readerSettingState.value
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
            readerController.setTagPopulation(_readerSettingState.value.tagPopulation.toShort())
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