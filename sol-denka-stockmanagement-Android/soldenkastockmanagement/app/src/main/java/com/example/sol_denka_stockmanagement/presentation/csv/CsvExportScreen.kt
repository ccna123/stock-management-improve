package com.example.sol_denka_stockmanagement.presentation.csv

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
import androidx.compose.runtime.getValue
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
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CsvExportScreen(
    csvViewModel: CsvViewModel,
    appViewModel: AppViewModel,
    onGoBack: () -> Unit
) {
    val uiState by csvViewModel.uiState.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.csvType) {
        when (uiState.csvType) {
            in listOf(
                CsvType.InboundResult.displayNameJp,
                CsvType.OutboundResult.displayNameJp,
                CsvType.LocationChangeResult.displayNameJp,
                CsvType.InventoryResult.displayNameJp,
            ) -> {
                csvViewModel.onIntent(CsvIntent.FetchExportCsvFiles)
                csvViewModel.onIntent(CsvIntent.ToggleProgressVisibility(false))
                csvViewModel.onIntent(CsvIntent.ResetFileSelect)
                csvViewModel.onIntent(CsvIntent.ResetFileSelectedStatus)
            }
            else -> csvViewModel.onIntent(CsvIntent.ClearCsvFileList)
        }
    }

    // Observe event → trigger global dialog
    LaunchedEffect(uiState.event) {
        when (uiState.event) {
            is CsvEvent.ExportSuccess ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.EXPORT_CSV_OK,
                        message = MessageMapper.toMessage(StatusCode.EXPORT_OK)
                    )
                )
            is CsvEvent.ExportFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.EXPORT_CSV_FAILED,
                        message = MessageMapper.toMessage(StatusCode.EXPORT_FAILED)
                    )
                )
            null -> Unit
        }
        if (uiState.event != null) {
            csvViewModel.onIntent(CsvIntent.EventConsumed)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            csvViewModel.onIntent(CsvIntent.ResetCsvType)
        }
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(ShareIntent.ToggleNetworkDialog(false))
        })
    }

    if (uiState.showProcessResultDialog) {
        AppDialog {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.processResultMessage ?: "",
                    textAlign = TextAlign.Center,
                    color = when (uiState.exportResultStatus) {
                        is ProcessResult.Failure -> Color.Red
                        is ProcessResult.Success -> brightGreenSecondary
                        null -> Color.Unspecified
                    }
                )
                Spacer(Modifier.height(12.dp))
                ButtonContainer(
                    containerColor = when (uiState.exportResultStatus) {
                        is ProcessResult.Failure -> Color.Red
                        is ProcessResult.Success -> brightAzure
                        null -> Color.Unspecified
                    },
                    buttonText = stringResource(R.string.close),
                    onClick = {
                        csvViewModel.onIntent(CsvIntent.DismissProcessResultDialog)
                    }
                )
            }
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
                    elevation = 13.dp,
                    clip = true,
                    ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.file_export),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                canClick = uiState.exportFileSelectedIndex != -1,
                onClick = {
                    csvViewModel.onIntent(
                        CsvIntent.ExportCsv(
                            sessionId = uiState.exportFileSessionId,
                            csvType = uiState.csvType
                        )
                    )
                },
            )
        },
        onBackArrowClick = { onGoBack() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
        ) {
            CardContainer {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputContainer(
                        title = "CSVファイルの種類選択",
                        isRequired = true,
                        children = {
                            ExposedDropdownMenuBox(
                                expanded = uiState.csvTypeExpanded,
                                onExpandedChange = {
                                    csvViewModel.onIntent(CsvIntent.ToggleCsvTypeExpanded)
                                }
                            ) {
                                InputFieldContainer(
                                    modifier = Modifier
                                        .menuAnchor(
                                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                            enabled = true
                                        )
                                        .fillMaxWidth(),
                                    value = if (uiState.csvType == SelectTitle.SelectCsvType.displayName) ""
                                    else uiState.csvType,
                                    hintText = SelectTitle.SelectCsvType.displayName,
                                    isNumeric = false,
                                    shape = RoundedCornerShape(13.dp),
                                    onChange = {},
                                    readOnly = true,
                                    isDropDown = true,
                                    enable = true,
                                )
                                ExposedDropdownMenu(
                                    expanded = uiState.csvTypeExpanded,
                                    onDismissRequest = {
                                        csvViewModel.onIntent(CsvIntent.ToggleCsvTypeExpanded)
                                    }
                                ) {
                                    listOf(
                                        SelectTitle.SelectCsvType.displayName,
                                        CsvType.InboundResult.displayNameJp,
                                        CsvType.OutboundResult.displayNameJp,
                                        CsvType.LocationChangeResult.displayNameJp,
                                        CsvType.InventoryResult.displayNameJp
                                    ).forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(text = type) },
                                            onClick = {
                                                csvViewModel.onIntent(
                                                    CsvIntent.SelectCsvType(
                                                        csvType = if (type == SelectTitle.SelectCsvType.displayName) "" else type
                                                    )
                                                )
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
                    if (uiState.csvType == "") {
                        if (uiState.exportFiles.isEmpty()) {
                            Text(
                                color = Color.Red,
                                fontSize = 17.sp,
                                text = stringResource(R.string.csv_type_select_request)
                            )
                        }
                    } else {
                        uiState.exportFiles
                            .sortedByDescending { it.timeStamp }
                            .takeIf { it.isNotEmpty() }
                            ?.forEachIndexed { index, file ->
                                val isSelectedFile = uiState.exportFileSelectedIndex == index
                                SingleCsvFile(
                                    csvFileName = file.fileName.substringBeforeLast("_") + "_" +
                                            formatTimestamp(file.timeStamp.substringAfterLast("_")),
                                    isSelected = isSelectedFile,
                                    timeStamp = file.timeStamp,
                                    type = CsvHistoryDirection.EXPORT.displayName,
                                    onChoose = {
                                        csvViewModel.onIntent(
                                            CsvIntent.ToggleFileSelect(
                                                type = "Export",
                                                fileIndex = index,
                                                fileSessionId = file.sessionId,
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