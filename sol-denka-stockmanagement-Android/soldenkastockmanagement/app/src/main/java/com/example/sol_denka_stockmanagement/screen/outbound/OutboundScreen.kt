package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.ScanResultTable
import com.example.sol_denka_stockmanagement.share.dialog.DateDialog
import com.example.sol_denka_stockmanagement.share.dialog.TimeDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

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

    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val processTypeMap by appViewModel.perTagProcessMethod.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.applyProcessType(processTypeMap)
    }

    TimeDialog(
        showTimeDialog = generalState.showTimePicker,
        title = stringResource(R.string.choose_time),
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { time ->
            appViewModel.onInputIntent(InputIntent.ChangeProcessedAtTime(time))
            appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
        },
        onDismissRequest = {
            appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
        }
    )

    DateDialog(
        showDateDialog = generalState.showDatePicker,
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { date -> appViewModel.onInputIntent(InputIntent.ChangeProcessedAtDate(date)) },
        onDismissRequest = { appViewModel.onGeneralIntent(ShareIntent.ToggleDatePicker(false)) }
    )

    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Outbound.routeId,
        prevScreenNameId = Screen.Outbound.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ButtonContainer(
                    buttonText = stringResource(R.string.cancel),
                    containerColor = Color.Red,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    onClick = {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ShowDialog(
                                type = DialogType.CONFIRM,
                                message = MessageMapper.toMessage(StatusCode.CANCEL)
                            )
                        )
                    }
                )
                ButtonContainer(
                    buttonText = stringResource(R.string.register),
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
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

                        val processedAt =
                            if (inputState.processedAtDate.isEmpty() || inputState.processedAtTime.isEmpty()) {
                                null
                            } else {
                                "${inputState.processedAtDate}T${inputState.processedAtTime}"
                            }
                        scope.launch {
                            val result = outboundViewModel.saveOutboundToDb(
                                memo = inputState.memo,
                                processedAt = processedAt,
                                registeredAt = generateIso8601JstTimestamp(),
                                rfidTagList = rfidTagList.filter { it.newFields.isChecked }
                            )
                            result.exceptionOrNull()?.let { e ->
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ShowDialog(
                                        type = DialogType.ERROR,
                                        message = MessageMapper.toMessage(StatusCode.FAILED)
                                    )
                                )
                                return@launch
                            }
                            val csvModels =
                                outboundViewModel.generateCsvData(
                                    memo = inputState.memo,
                                    processedAt = processedAt,
                                    registeredAt = generateIso8601JstTimestamp(),
                                    rfidTagList = rfidTagList.filter { it.newFields.isChecked }
                                )
                            val saveResult = appViewModel.saveScanResultToCsv(
                                data = csvModels,
                                direction = CsvHistoryDirection.EXPORT,
                                taskCode = CsvTaskType.OUT,
                            )
                            saveResult
                                .onSuccess {
                                    // SAVE CSV success
                                    // Now check network and send SFTP
                                    if (isNetworkConnected) {
                                        //sftp send
                                    } else {
                                        appViewModel.onGeneralIntent(
                                            ShareIntent.ShowDialog(
                                                type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                                                message = MessageMapper.toMessage(StatusCode.EXPORT_OK)
                                            )
                                        )
                                    }
                                }
                                .onFailure { throwable ->
                                    val code = StatusCode.valueOf(throwable.message!!)
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ShowDialog(
                                            type = DialogType.ERROR,
                                            message = MessageMapper.toMessage(code)
                                        )
                                    )
                                }
                        }
                    }
                )
            }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InputFieldContainer(
                            modifier = Modifier.weight(1f),
                            value = inputState.processedAtDate,
                            label = stringResource(R.string.processed_at_date),
                            isNumeric = false,
                            shape = RoundedCornerShape(13.dp),
                            readOnly = true,
                            isDropDown = false,
                            enable = false,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = brightAzure,
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleDatePicker(
                                                    true
                                                )
                                            )
                                        }
                                    )
                                )
                            }
                        )
                        InputFieldContainer(
                            modifier = Modifier.weight(1f),
                            value = inputState.processedAtTime,
                            label = stringResource(R.string.processed_at_time),
                            isNumeric = false,
                            shape = RoundedCornerShape(13.dp),
                            readOnly = true,
                            isDropDown = false,
                            enable = false,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = brightAzure,
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleTimePicker(
                                                    true
                                                )
                                            )
                                        }
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    InputFieldContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        value = inputState.memo,
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