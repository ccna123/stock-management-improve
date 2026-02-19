package com.example.sol_denka_stockmanagement.screen.csv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.CsvIntent
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.csv.components.SingleCsvFile
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.CardContainer
import com.example.sol_denka_stockmanagement.share.InputContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.AppDialog
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CsvExportScreen(
    csvViewModel: CsvViewModel,
    appViewModel: AppViewModel,
    onGoBack: () -> Unit
) {
    val csvType by csvViewModel.csvType.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsState()

    val exportFiles by csvViewModel.exportFiles.collectAsStateWithLifecycle()
    val exportFileSessionId by csvViewModel.exportFileSessionId.collectAsStateWithLifecycle()
    val isExporting by csvViewModel.isExporting.collectAsStateWithLifecycle()

    val exportFileSelectedIndex by csvViewModel.exportFileSelectedIndex.collectAsState()
    val showProcessResultDialog by csvViewModel.showProcessResultDialog.collectAsStateWithLifecycle()
    val processResultMessage by csvViewModel.processResultMessage.collectAsStateWithLifecycle()

    val exportResultStatus by csvViewModel.exportResultStatus.collectAsStateWithLifecycle()
    val csvModels = csvViewModel.csvModels
    val scope = rememberCoroutineScope()

    LaunchedEffect(csvType) {
        when (csvType) {
            in listOf(
                CsvType.InboundResult.displayNameJp,
                CsvType.OutboundResult.displayNameJp,
                CsvType.LocationChangeResult.displayNameJp,
                CsvType.InventoryResult.displayNameJp,
            ) -> {
                csvViewModel.apply {
                    onCsvIntent(CsvIntent.FetchExportCsvFiles)
                    onCsvIntent(CsvIntent.ToggleProgressVisibility(false))
                    onCsvIntent(CsvIntent.ResetFileSelect)
                    onCsvIntent(CsvIntent.ResetFileSelectedStatus)
                }
            }

            else -> csvViewModel.onCsvIntent(CsvIntent.ClearCsvFileList)
        }
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(
                ShareIntent.ToggleNetworkDialog(false)
            )
        })
    }

    if (showProcessResultDialog) {
        AppDialog {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = processResultMessage ?: "",
                    textAlign = TextAlign.Center,
                    color = when (exportResultStatus) {
                        is ProcessResult.Failure -> Color.Red
                        is ProcessResult.Success -> brightGreenSecondary
                        null -> Color.Unspecified
                    }
                )
                Spacer(Modifier.height(12.dp))
                ButtonContainer(
                    containerColor = when (exportResultStatus) {
                        is ProcessResult.Failure -> Color.Red
                        is ProcessResult.Success -> brightAzure
                        null -> Color.Unspecified
                    },
                    buttonText = stringResource(R.string.close),
                    onClick = {
                        csvViewModel.dismissProcessResultDialog()
                    }
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            csvViewModel.onCsvIntent(CsvIntent.ResetCsvType)
        }
    }

    Layout(
        topBarText = "${Screen.fromRouteId(Screen.CsvExport.routeId)?.displayName}",
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                buttonTextSize = 20,
                buttonText = stringResource(R.string.export_file),
                canClick = csvType.isNotEmpty() && exportFiles.isNotEmpty() && exportFileSelectedIndex != -1,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.file_export),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                onClick = {
                    scope.launch {
                        val data = csvViewModel.getEventDataBySessionId(
                            sessionId = exportFileSessionId,
                            type = csvType
                        )
                        val saveResult = appViewModel.saveScanResultToCsv(
                            data = data,
                            direction = CsvHistoryDirection.EXPORT,
                            taskCode = CsvTaskType.IN,
                        )

                        if (saveResult) {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ShowDialog(
                                    type = DialogType.EXPORT_CSV_OK,
                                    message = MessageMapper.toMessage(StatusCode.EXPORT_OK)
                                )
                            )
//                            if (isNetworkConnected) {
//                                    //sftp send
//                                } else {
//                                    appViewModel.onGeneralIntent(
//                                        ShareIntent.ShowDialog(
//                                            type = DialogType.SAVE_CSV_SEND_SFTP_SUCCESS,
//                                            message = MessageMapper.toMessage(StatusCode.SAVE_CSV_SEND_SFTP_SUCCESS)
//                                        )
//                                    )
//                                }
                        } else {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ShowDialog(
                                    type = DialogType.EXPORT_CSV_FAILED,
                                    message = MessageMapper.toMessage(StatusCode.EXPORT_FAILED)
                                )
                            )
                        }
                    }
                },
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
        ) {
            CardContainer {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    InputContainer(
                        title = "CSVファイルの種類選択",
                        isRequired = true,
                        children = {
                            ExposedDropdownMenuBox(
                                expanded = expandState.csvTypeExpanded,
                                onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleCsvTypeExpanded) }) {
                                InputFieldContainer(
                                    modifier = Modifier
                                        .menuAnchor(
                                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                            enabled = true
                                        )
                                        .fillMaxWidth(),
                                    value = if (csvType == SelectTitle.SelectCsvType.displayName) "" else csvType,
                                    hintText = SelectTitle.SelectCsvType.displayName,
                                    isNumeric = false,
                                    shape = RoundedCornerShape(13.dp),
                                    onChange = {
                                        csvViewModel.onCsvIntent(
                                            CsvIntent.SelectCsvType(
                                                csvType = it
                                            )
                                        )
                                    },
                                    readOnly = true,
                                    isDropDown = true,
                                    enable = true,
                                )
                                ExposedDropdownMenu(
                                    expanded = expandState.csvTypeExpanded,
                                    onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleCsvTypeExpanded) }
                                ) {
                                    listOf(
                                        SelectTitle.SelectCsvType.displayName,
                                        CsvType.InboundResult.displayNameJp,
                                        CsvType.OutboundResult.displayNameJp,
                                        CsvType.LocationChangeResult.displayNameJp,
                                        CsvType.InventoryResult.displayNameJp
                                    ).forEach { csvType ->
                                        DropdownMenuItem(
                                            text = { Text(text = csvType) },
                                            onClick = {
                                                csvViewModel.onCsvIntent(
                                                    CsvIntent.SelectCsvType(
                                                        csvType = if (csvType == SelectTitle.SelectCsvType.displayName) "" else csvType
                                                    )
                                                )
                                                appViewModel.onExpandIntent(ExpandIntent.ToggleCsvTypeExpanded)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (csvType == "") {
                        if (exportFiles.isEmpty()) {
                            Text(
                                color = Color.Red,
                                fontSize = 17.sp,
                                text = stringResource(R.string.csv_type_select_request)
                            )
                        }
                    } else {
                        exportFiles.takeIf { it.isNotEmpty() }?.forEachIndexed { index, sessionModel ->
                            val isSelectedFile = exportFileSelectedIndex == index
                            SingleCsvFile(
                                csvFileName =
                                    sessionModel.timeStamp.substringBeforeLast("_") + "_" +
                                            formatTimestamp(sessionModel.timeStamp.substringAfterLast("_")),
                                isSelected = isSelectedFile,
                                timeStamp = sessionModel.timeStamp.substringAfterLast("_"),
                                type = CsvHistoryDirection.EXPORT.displayName,
                                onChoose = {
                                    csvViewModel.onCsvIntent(
                                        CsvIntent.ToggleFileSelect(
                                            type = "Export",
                                            fileIndex = index,
                                            fileSessionId = sessionModel.sessionId,
                                        )
                                    )
                                }
                            )
                        } ?: Text(
                            color = Color.Red,
                            text = stringResource(R.string.no_csv_file_found)
                        )
                    }
                }
            }
        }
    }
}