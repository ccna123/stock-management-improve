package com.example.sol_denka_stockmanagement.screen.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.helper.ToastManager
import com.example.sol_denka_stockmanagement.helper.ToastType
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingScreen
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.OptionTabs
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingScreen(
    appViewModel: AppViewModel,
    settingViewModel: SettingViewModel,
    onGoBack: () -> Unit
) {
    val generalState = appViewModel.generalState.collectAsStateWithLifecycle().value
    val readerSettingState = settingViewModel.readerSettingState.collectAsStateWithLifecycle().value
//    val appSettingState by appSettingViewModel.appSettingState.collectAsStateWithLifecycle()
    var showApplyConfirmDialog by remember { mutableStateOf(false) }
    var showUnsavedConfirmDialog by remember { mutableStateOf(false) }

    ConfirmDialog(
        showDialog = showUnsavedConfirmDialog,
        dialogTitle = stringResource(R.string.setting_interrupt_confirm),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    onClick = {
                        showUnsavedConfirmDialog = false
                        settingViewModel.resetToInitialSetting()
                        onGoBack()
                    })
            }, {
                ButtonContainer(
                    containerColor = Color.Red,
                    buttonText = stringResource(R.string.close),
                    onClick = {
                        showUnsavedConfirmDialog = false
                    })
            }
        )
    )

    ConfirmDialog(
        showDialog = showApplyConfirmDialog,
        dialogTitle = stringResource(R.string.setting_apply_confirm),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    onClick = {
                        val result = settingViewModel.applyReaderSetting()
                        when (result) {
                            true -> ToastManager.showToast("設定に成功しました", ToastType.SUCCESS)
                            false -> ToastManager.showToast("設定に失敗しました", ToastType.ERROR)
                        }
                        showApplyConfirmDialog = false
                    }
                )
            }, {
                ButtonContainer(
                    containerColor = Color.Red,
                    buttonText = stringResource(R.string.close),
                    onClick = {
                        showApplyConfirmDialog = false
                    }
                )
            }
        )
    )
    Layout(
        topBarText = Screen.Setting.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.Setting.routeId,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.fillMaxWidth(.5f),
                onClick = {
                    showApplyConfirmDialog = true
                },
                buttonText = stringResource(R.string.apply_setting),
            )
        },
        onBackArrowClick = {
            if (settingViewModel.isSettingChangedWithoutApply()) {
                showUnsavedConfirmDialog = true
            } else {
                settingViewModel.resetToInitialSetting()
                onGoBack()
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .padding(
                    paddingValues = PaddingValues(
                        top = 16.dp,
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp))
            ) {
                OptionTabs(
                    leftTabText = stringResource(R.string.reader_setting),
                    rightTabText = stringResource(R.string.app_setting),
                    leftTab = Tab.Left,
                    rightTab = Tab.Right,
                    leftIcon = Icons.Default.QrCodeScanner,
                    rightIcon = Icons.Default.AppSettingsAlt,
                    tab = generalState.tab,
                    onChangeTab = {
                        appViewModel.onGeneralIntent(ShareIntent.ChangeTab(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = brightAzure)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                AnimatedContent(
                    targetState = generalState.tab,
                    transitionSpec = {
                        if (targetState == Tab.Left) {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                        } else {
                            slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                        }
                    },
                    label = "SettingTabAnimation"
                ) { tab ->
                    when (tab) {
                        Tab.Left -> {
                            ReaderSettingScreen(
                                buzzerVolume = readerSettingState.buzzerVolume,
                                radioPower = readerSettingState.radioPower,
                                radioPowerMw = readerSettingState.radioPowerMw,
                                radioPowerSliderPosition = readerSettingState.radioPowerSliderPosition,
                                session = readerSettingState.rfidSession,
                                tagPopulation = readerSettingState.tagPopulation,
                                tagAccessFlag = readerSettingState.tagAccessFlag,
                                enabledChannels = readerSettingState.enabledRfidChannel,
                                supportedChannels = readerSettingState.supportedChannels,
                                onChangeVolume = { volume ->
                                    settingViewModel.onReaderSettingIntent(
                                        ReaderSettingIntent.ChangeVolume(
                                            volume
                                        )
                                    )
                                },
                                onChangePower = {},
                                onChangeMinPower = {
                                    settingViewModel.apply {
                                        onReaderSettingIntent(
                                            ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                                0
                                            )
                                        )
                                        onReaderSettingIntent(ReaderSettingIntent.ChangeRadioPower(0))
                                    }
                                },
                                onChangeMaxPower = {
                                    settingViewModel.apply {
                                        onReaderSettingIntent(
                                            ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                                30
                                            )
                                        )
                                        onReaderSettingIntent(
                                            ReaderSettingIntent.ChangeRadioPower(
                                                30
                                            )
                                        )
                                    }
                                },
                                onChangeSlider = {
                                    val intValue = it.coerceIn(0, 30)
                                    settingViewModel.apply {
                                        onReaderSettingIntent(
                                            ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                                intValue
                                            )
                                        )
                                        onReaderSettingIntent(
                                            ReaderSettingIntent.ChangeRadioPower(
                                                intValue
                                            )
                                        )
                                    }
                                },
                                onChangeSession = {
                                    settingViewModel.onReaderSettingIntent(
                                        ReaderSettingIntent.ChangeSession(
                                            it
                                        )
                                    )
                                },
                                onChangeTagAccessFlag = {
                                    settingViewModel.onReaderSettingIntent(
                                        ReaderSettingIntent.ChangeTagAccessFlag(
                                            it
                                        )
                                    )
                                },
                                onChangeChannel = { channel ->
                                    val updated =
                                        if (channel in readerSettingState.enabledRfidChannel) {
                                            readerSettingState.enabledRfidChannel - channel
                                        } else {
                                            readerSettingState.enabledRfidChannel + channel
                                        }

                                    settingViewModel.onReaderSettingIntent(
                                        ReaderSettingIntent.ChangeUsedChannel(updated)
                                    )
                                },
                                onChangeTagPopulation = {
                                    val filteredValue =
                                        it.filter { char -> char.isDigit() || char == '.' }
                                    settingViewModel.onReaderSettingIntent(
                                        ReaderSettingIntent.ChangeTagPopulation(
                                            filteredValue
                                        )
                                    )
                                },
                            )
                        }

                        Tab.Right -> {
//                            AppSettingScreen(
//                                appSettingViewModel = appSettingViewModel,
//                                appViewModel = appViewModel
//                            )
                        }
                    }
                }
            }
        }
    }
}

