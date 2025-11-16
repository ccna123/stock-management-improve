package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.screen.setting.components.app_setting_tab.AppSettingRow
import com.example.sol_denka_stockmanagement.share.ButtonType
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.component.ConnectionStatusRow
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ExpandableSettingSection
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightGreen
import com.example.sol_denka_stockmanagement.ui.theme.skyBlue
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
    var isFileTransferMethodExpanded by remember { mutableStateOf(false) }
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
                    ExpandableSettingSection(
                        title = stringResource(R.string.setting_file_transfer_method),
                        value = appViewModel.inputState.value.fileTransferMethod,
                        expanded = isFileTransferMethodExpanded,
                        onExpandToggle = { isFileTransferMethodExpanded = !isFileTransferMethodExpanded },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    FileTransferMethod.WIFI.displayName,
                                    FileTransferMethod.USB.displayName,
                                ).forEach { method ->
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = if (appViewModel.inputState.value.fileTransferMethod == method) skyBlue.copy(
                                                    alpha = 0.4f
                                                ) else Color.Unspecified, shape = RoundedCornerShape(10.dp)
                                            )
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        RadioButton(
                                            selected = appViewModel.inputState.value.fileTransferMethod == method,
                                            onClick = {
                                                appViewModel.onInputIntent(
                                                    InputIntent.ChangeFileTransferMethod(
                                                        method
                                                    )
                                                )
                                            },
                                            enabled = true,
                                        )
                                        DropdownMenuItem(
                                            text = { Text(method) },
                                            onClick = {
                                                appViewModel.onInputIntent(
                                                    InputIntent.ChangeFileTransferMethod(
                                                        method
                                                    )
                                                )
                                            }
                                        )
                                    }

                                }
                            }
                        }
                    )
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
        HorizontalDivider()
    }
}