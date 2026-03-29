package com.example.sol_denka_stockmanagement.presentation.outbound

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.domain.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ShareIntent
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
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OutboundScreen(
    appViewModel: AppViewModel,
    outboundViewModel: OutboundViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit
) {
    val uiState by outboundViewModel.uiState.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val processTypeMap by appViewModel.perTagProcessMethod.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.applyProcessType(processTypeMap)
    }

    LaunchedEffect(uiState.event) {
        when (uiState.event) {
            is OutboundEvent.SaveDbFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.ERROR,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_DB_FAILED)
                    )
                )
            is OutboundEvent.SaveCsvSuccess ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                        message = MessageMapper.toMessage(StatusCode.SAVE_CSV_SUCCESS_FAILED_SFTP)
                    )
                )
            is OutboundEvent.SaveCsvFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_FAILED,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_CSV_FAILED)
                    )
                )
            null -> Unit
        }
        if (uiState.event != null) {
            outboundViewModel.onIntent(OutboundIntent.EventConsumed)
        }
    }

    TimeDialog(
        showTimeDialog = uiState.showTimePicker,
        title = stringResource(R.string.choose_time),
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { time ->
            outboundViewModel.onIntent(OutboundIntent.ProcessedAtTimeChanged(time))
            outboundViewModel.onIntent(OutboundIntent.ToggleTimePicker(false))
        },
        onDismissRequest = {
            outboundViewModel.onIntent(OutboundIntent.ToggleTimePicker(false))
        }
    )

    DateDialog(
        showDateDialog = uiState.showDatePicker,
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { date ->
            outboundViewModel.onIntent(OutboundIntent.ProcessedAtDateChanged(date))
            outboundViewModel.onIntent(OutboundIntent.ToggleDatePicker(false))
        },
        onDismissRequest = {
            outboundViewModel.onIntent(OutboundIntent.ToggleDatePicker(false))
        }
    )

    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        hasBottomBar = true,
        retrySaveDb = {
            outboundViewModel.onIntent(OutboundIntent.Retry)
        },
        bottomButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ButtonContainer(
                    buttonText = stringResource(R.string.cancel),
                    containerColor = Color.Red,
                    modifier = Modifier.weight(1f).shadow(elevation = 13.dp, clip = true),
                    onClick = {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ShowDialog(
                                type = DialogType.CANCEL_OPERATION,
                                message = MessageMapper.toMessage(StatusCode.CANCEL)
                            )
                        )
                    }
                )
                ButtonContainer(
                    buttonText = stringResource(R.string.register),
                    modifier = Modifier.weight(1f).shadow(elevation = 13.dp, clip = true),
                    onClick = {
                        val selectedTags = rfidTagList.filter { it.newFields.isChecked }
                        val now = generateIso8601JstTimestamp()
                        val processedAt = if (uiState.processedAtDate.isEmpty() || uiState.processedAtTime.isEmpty()) {
                            null
                        } else {
                            "${uiState.processedAtDate}T${uiState.processedAtTime}"
                        }

                        outboundViewModel.onIntent(
                            OutboundIntent.Execute(
                                memo = uiState.memo,
                                processedAt = processedAt,
                                registeredAt = now,
                                executedAt = now,
                                sourceEventIdByTagId = selectedTags.associate {
                                    it.tagId to UUID.randomUUID().toString()
                                },
                                rfidTagList = selectedTags
                            )
                        )
                    }
                )
            }
        },
        onBackArrowClick = { onGoBack() }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues).padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.planned_register_item_number,
                    rfidTagList.count { it.newFields.isChecked }
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn(modifier = Modifier.imePadding()) {
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
                                itemName = tag.newFields.itemName,
                                itemCode = tag.epc,
                                lastColumn = tag.newFields.processType
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InputFieldContainer(
                            modifier = Modifier.weight(1f),
                            value = uiState.processedAtDate,
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
                                    modifier = Modifier.clickable {
                                        outboundViewModel.onIntent(OutboundIntent.ToggleDatePicker(true))
                                    }
                                )
                            }
                        )
                        InputFieldContainer(
                            modifier = Modifier.weight(1f),
                            value = uiState.processedAtTime,
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
                                    modifier = Modifier.clickable {
                                        outboundViewModel.onIntent(OutboundIntent.ToggleTimePicker(true))
                                    }
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    InputFieldContainer(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        value = uiState.memo,
                        label = "${stringResource(R.string.memo)} (オプション)",
                        hintText = stringResource(R.string.memo_hint),
                        isNumeric = false,
                        shape = RoundedCornerShape(13.dp),
                        readOnly = false,
                        isDropDown = false,
                        enable = true,
                        singleLine = false,
                        onChange = { outboundViewModel.onIntent(OutboundIntent.MemoChanged(it)) }
                    )
                }
            }
        }
    }
}