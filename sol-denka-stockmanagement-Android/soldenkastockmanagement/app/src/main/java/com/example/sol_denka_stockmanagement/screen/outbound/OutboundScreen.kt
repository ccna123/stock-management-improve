package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.ScanResultTable
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OutboundScreen(
    appViewModel: AppViewModel,
    outboundViewModel: OutboundViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState = appViewModel.inputState.collectAsStateWithLifecycle()
    val generalState = appViewModel.generalState.collectAsStateWithLifecycle()
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle().value
    val processTypeMap by appViewModel.perTagProcessMethod.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.applyProcessType(processTypeMap)
    }

    if (generalState.value.showTimePicker) {
        val current = Calendar.getInstance()
        val state = rememberTimePickerState(
            initialHour = current.get(Calendar.HOUR_OF_DAY),
            initialMinute = current.get(Calendar.MINUTE),
            is24Hour = true
        )

        TimePickerDialog(
            title = { Text("時刻を選択") },
            onDismissRequest = {
                appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
            },
            confirmButton = {
                TextButton(onClick = {
                    val timeStr = String.format(Locale.US, "%02d:%02d", state.hour, state.minute)
                    appViewModel.onInputIntent(InputIntent.ChangeOccurredAt(timeStr))
                    appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
                }) {
                    Text("キャンセル")
                }
            }
        ) {
            TimePicker(state = state)
        }
    }
    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Outbound.routeId,
        prevScreenNameId = Screen.Outbound.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    scope.launch {
                        outboundViewModel.saveOutboundToDb(
                            memo = inputState.value.memo,
                            occurredAt = inputState.value.occurredAt,
                            rfidTagList = rfidTagList.filter { it.newFields.isChecked }
                        )
                        val csvModels =
                            outboundViewModel.generateCsvData(
                                memo = inputState.value.memo,
                                rfidTagList = rfidTagList.filter { it.newFields.isChecked }
                            )
                        appViewModel.onGeneralIntent(
                            ShareIntent.SaveScanResult(
                                data = csvModels,
                                direction = CsvHistoryDirection.EXPORT,
                                taskCode = CsvTaskType.OUT
                            )
                        )
                    }
                },
                buttonText = stringResource(R.string.register),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.planned_register_item_number,
                    rfidTagList.count { it.newFields.isChecked }
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn(
                modifier = Modifier
                    .imePadding()
            ) {
                item {
                    ScanResultTable(
                        tableHeight = 250.dp,
                        column3Weight = .5f,
                        tableHeader = listOf(
                            stringResource(R.string.item_name_title),
                            stringResource(R.string.item_code_title),
                            stringResource(R.string.process_method)
                        ),
                        scanResult = rfidTagList.filter { it.newFields.isChecked }.map { tag ->
                            ScanResultRowModel(
                                itemName = rfidTagList.find { it.epc == tag.epc }?.epc ?: "-",
                                itemCode = rfidTagList.find { it.epc == tag.epc }?.newFields?.itemCode
                                    ?: "-",
                                lastColumn = rfidTagList.find { it.epc == tag.epc }?.newFields?.processType
                                    ?: "-",
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(true))
                            }
                    ) {
                        InputFieldContainer(
                            modifier = Modifier.fillMaxWidth(),
                            value = inputState.value.occurredAt,
                            label = stringResource(R.string.occurred_at),
                            isNumeric = false,
                            shape = RoundedCornerShape(13.dp),
                            readOnly = true,
                            isDropDown = false,
                            enable = false,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    InputFieldContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        value = inputState.value.memo,
                        label = "${stringResource(R.string.memo)} (オプション)",
                        hintText = stringResource(R.string.memo_hint),
                        isNumeric = false,
                        shape = RoundedCornerShape(13.dp),
                        readOnly = false,
                        isDropDown = false,
                        enable = true,
                        singleLine = false,
                        onChange = { newValue ->
                            appViewModel.onInputIntent(
                                InputIntent.ChangeMemo(
                                    newValue
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}