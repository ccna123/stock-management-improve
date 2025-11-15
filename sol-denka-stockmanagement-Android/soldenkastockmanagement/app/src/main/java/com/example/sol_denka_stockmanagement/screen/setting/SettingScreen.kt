package com.example.sol_denka_stockmanagement.screen.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.helper.ToastManager
import com.example.sol_denka_stockmanagement.helper.ToastType
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingScreen
import com.example.sol_denka_stockmanagement.share.AppDialog
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.OptionTabs
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingScreen(
    appViewModel: AppViewModel,
    appSettingViewModel: AppSettingViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    onNavigate: (Screen) -> Unit
) {
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
//    val appSettingState by appSettingViewModel.appSettingState.collectAsStateWithLifecycle()
    var showApplyConfirmDialog by remember { mutableStateOf(false) }
    var showUnsavedConfirmDialog by remember { mutableStateOf(false) }

    if (showUnsavedConfirmDialog) {
        AppDialog{
            Column {
                Text(
                    text = stringResource(R.string.setting_interrupt_confirm),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonContainer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        buttonText = stringResource(R.string.ok),
                        onClick = {
                            showUnsavedConfirmDialog = false
                            readerSettingViewModel.resetToInitialSetting()
                            onNavigate(Screen.Home)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ButtonContainer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        containerColor = Color.Red,
                        buttonText = stringResource(R.string.close),
                        onClick = {
                            showUnsavedConfirmDialog = false
                        }
                    )
                }
            }
        }
    }
    if (showApplyConfirmDialog) {
        AppDialog{
            Column {
                Text(
                    text = stringResource(R.string.setting_apply_confirm),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonContainer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        buttonText = stringResource(R.string.ok),
                        onClick = {
                            val result = readerSettingViewModel.applyReaderSetting()
                            when(result){
                                true -> ToastManager.showToast("設定に成功しました", ToastType.SUCCESS)
                                false -> ToastManager.showToast("設定に失敗しました", ToastType.ERROR)
                            }
                            showApplyConfirmDialog = false
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ButtonContainer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        containerColor = Color.Red,
                        buttonText = stringResource(R.string.close),
                        onClick = {
                            showApplyConfirmDialog = false
                        }
                    )
                }
            }
        }
    }
//    if (showSettingAppliedFailedDialog) {
//        AppDialog(
//            modifier = Modifier.fillMaxWidth(),
//            buttons = listOf(
//                DialogButton(
//                    text = stringResource(R.string.close), action = {
//                        showSettingAppliedFailedDialog = false
//                    }),
//            ),
//        ) {
//            Text(
//                color = Color.Red, text = stringResource(
//                    R.string.apply_setting_fail,
//                ), textAlign = TextAlign.Center, // Center the text itself
//                modifier = Modifier.padding(bottom = 16.dp) // Space below text
//            )
//        }
//    }
//    if (showNetworkDisconnectDialog) {
//        AppDialog(
//            modifier = Modifier.fillMaxWidth(),
//            buttons = listOf(
//                DialogButton(
//                    text = stringResource(R.string.close),
//                    action = { showNetworkDisconnectDialog = false }),
//            ),
//        ) {
//            Text(
//                text = stringResource(R.string.setting_network_not_connect),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//        }
//    }
//    if (showDeleteStatusDialog) {
//        AppDialog(
//            modifier = Modifier.fillMaxWidth(),
//            buttons = listOf(
//                DialogButton(
//                    text = stringResource(R.string.close), action = {
//                        showDeleteStatusDialog = false
//                        appSettingViewModel.apply {
//                            onAppSettingIntent(AppSettingIntent.ResetDeleteSuccess)
//                        }
//                    }
//                ),
//            ),
//        ) {
//            Text(
//                text = if (appSettingState.isDeleteSuccess) stringResource(R.string.dialog_delete_scan_data_success)
//                else stringResource(R.string.dialog_delete_scan_data_failed)
//            )
//        }
//    }
    Layout(
        topBarText = Screen.Setting.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Setting.routeId,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier,
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    showApplyConfirmDialog = true
                },
                buttonText = stringResource(R.string.apply_setting),
            )
        },
        onBackArrowClick = {
            if (readerSettingViewModel.isSettingChangedWithoutApply()){
                showUnsavedConfirmDialog = true
            }else {
                readerSettingViewModel.resetToInitialSetting()
                onNavigate(Screen.Home)
            }
        }) { paddingValues ->
        SettingScreenContent(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            appViewModel = appViewModel,
            appSettingViewModel = appSettingViewModel,
            generalState = generalState,
//            appSettingState = appSettingState,
            readerSettingViewModel = readerSettingViewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingScreenContent(
    modifier: Modifier,
    appViewModel: AppViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    appSettingViewModel: AppSettingViewModel,
//    appSettingState: AppSettingState,
    generalState: GeneralState,
) {
    Column(
        modifier = modifier
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
        HorizontalDivider()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            when (generalState.tab) {
                Tab.Left -> {
                    ReaderSettingScreen(
                        readerSettingViewModel = readerSettingViewModel
                    )
                }

                Tab.Right -> {
                    AppSettingScreen(
                        appSettingViewModel = appSettingViewModel,
                        onAutoConnectCheck = {
                        },
                        appViewModel = appViewModel,
                    )
                }
            }
        }
    }
}

