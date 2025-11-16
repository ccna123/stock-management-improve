package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.screen.setting.components.app_setting_tab.AppSettingRow
import com.example.sol_denka_stockmanagement.share.ButtonType
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.component.ConnectionStatusRow
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightGreen
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppSettingScreen(
    appSettingViewModel: AppSettingViewModel,
    appViewModel: AppViewModel,
) {
    val focusManager = LocalFocusManager.current
    val isNetworkConnected by appSettingViewModel.isNetworkConnected.collectAsStateWithLifecycle()
    val usbState by appSettingViewModel.usbState.collectAsStateWithLifecycle()
    val fileTransferMethod = appViewModel.inputState.value.fileTransferMethod
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Auto connect
        AppSettingRow(
            content = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column {
                            Text(text = stringResource(R.string.setting_file_transfer_method))
                            Text(
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                text = stringResource(R.string.setting_file_transfer_method_desc)
                            )
                        }
                        ExposedDropdownMenuBox(
                            expanded = appViewModel.expandState.value.fileTransferMethodExpanded,
                            onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleFileTransferMethodExpanded) }) {
                            InputFieldContainer(
                                modifier = Modifier
                                    .menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    )
                                    .fillMaxWidth(),
                                value = if (appViewModel.inputState.value.fileTransferMethod == SelectTitle.SelectHandlingMethod.displayName) "" else appViewModel.inputState.value.fileTransferMethod,
                                isNumeric = false,
                                hintText = SelectTitle.SelectHandlingMethod.displayName,
                                shape = RoundedCornerShape(13.dp),
                                onChange = { newValue ->
                                    appViewModel.onInputIntent(
                                        InputIntent.ChangeFileTransferMethod(
                                            newValue
                                        )
                                    )
                                },
                                readOnly = true,
                                isDropDown = true,
                                enable = true,
                                onClick = {
                                    appViewModel.onExpandIntent(ExpandIntent.ToggleFileTransferMethodExpanded)
                                },
                            )
                            ExposedDropdownMenu(
                                expanded = appViewModel.expandState.value.fileTransferMethodExpanded,
                                onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleFileTransferMethodExpanded) }
                            ) {
                                listOf(
                                    FileTransferMethod.SELECTION_TITLE.displayName,
                                    FileTransferMethod.USB.displayName,
                                    FileTransferMethod.WIFI.displayName,
                                ).forEach { method ->
                                    DropdownMenuItem(
                                        text = { Text(text = method) },
                                        onClick = {
                                            appViewModel.apply {
                                                onInputIntent(
                                                    InputIntent.ChangeFileTransferMethod(
                                                        if (method == FileTransferMethod.SELECTION_TITLE.displayName) "" else method
                                                    )
                                                )
                                                onExpandIntent(ExpandIntent.ToggleFileTransferMethodExpanded)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    when(fileTransferMethod){
                        FileTransferMethod.USB.displayName -> ConnectionStatusRow(
                            label = stringResource(R.string.connect_state),
                            isConnected = usbState.connected,
                            connectedText = stringResource(R.string.ok_connect),
                            connectedIcon = Icons.Default.Usb,
                            disconnectedText = stringResource(R.string.no_connect),
                            disconnectedIcon = Icons.Default.UsbOff
                        )
                        FileTransferMethod.WIFI.displayName -> ConnectionStatusRow(
                            label = stringResource(R.string.connect_state),
                            isConnected = isNetworkConnected,
                            connectedText = stringResource(R.string.ok_connect),
                            connectedIcon = Icons.Default.Wifi,
                            disconnectedText = stringResource(R.string.no_connect),
                            disconnectedIcon = Icons.Default.WifiOff
                        )
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
    }
}