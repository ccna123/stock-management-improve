package com.example.sol_denka_stockmanagement.screen.csv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.helper.toast.ToastManager
import com.example.sol_denka_stockmanagement.helper.toast.ToastType
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
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CsvExportScreen(
    csvViewModel: CsvViewModel,
    appViewModel: AppViewModel,
    onGoBack: () -> Unit
) {
    val csvType by csvViewModel.csvType.collectAsStateWithLifecycle()
    val expandState = appViewModel.expandState.collectAsStateWithLifecycle()
    val generalState = appViewModel.generalState.collectAsState().value
    val csvFiles by csvViewModel.csvFiles.collectAsStateWithLifecycle()
    val showProgress by csvViewModel.showProgress.collectAsStateWithLifecycle()
    val isExporting by csvViewModel.isExporting.collectAsStateWithLifecycle()

    LaunchedEffect(csvType) {
        when (csvType) {
            in listOf(
                CsvType.InboundResult.displayName,
                CsvType.OutboundResult.displayName,
                CsvType.LocationChangeResult.displayName,
                CsvType.InventoryResult.displayName,
            ) -> {
                csvViewModel.apply {
                    onCsvIntent(CsvIntent.FetchCsvFiles)
                    onCsvIntent(CsvIntent.ToggleProgressVisibility(false))
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
                canClick = csvType.isNotEmpty() && isExporting.not() && csvFiles.isNotEmpty(),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.file_export),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                onClick = {
                    ToastManager.showToast(
                        message = "開発中",
                        type = ToastType.INFO
                    )
//                    if (appViewModel.isNetworkConnected.value.not()) {
//                        appViewModel.onGeneralIntent(
//                            ShareIntent.ToggleNetworkDialog(true)
//                        )
//                    } else {
//                        csvViewModel.toggleProgressVisibility(true)
//                        csvViewModel.exportAllFilesIndividually(
//                            context = context,
//                            isInventoryResult = when (csvType) {
//                                CsvType.InventoryResult.displayName -> {
//                                    true
//                                }
//
//                                CsvType.StockEvent.displayName -> {
//                                    false
//                                }
//
//                                else -> false
//                            }
//                        )
//                    }
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
                                expanded = expandState.value.csvTypeExpanded,
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
                                    expanded = expandState.value.csvTypeExpanded,
                                    onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleCsvTypeExpanded) }
                                ) {
                                    listOf(
                                        SelectTitle.SelectCsvType.displayName,
                                        CsvType.InboundResult.displayName,
                                        CsvType.OutboundResult.displayName,
                                        CsvType.LocationChangeResult.displayName,
                                        CsvType.InventoryResult.displayName
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
                        if (csvFiles.isEmpty()) {
                            Text(
                                color = Color.Red,
                                fontSize = 17.sp,
                                text = stringResource(R.string.csv_type_select_request)
                            )
                        }
                    } else {
                        csvFiles.takeIf { it.isNotEmpty() }?.forEachIndexed { index, file ->
                            SingleCsvFile(
                                csvFileName = file.fileName,
                                csvFileSize = file.fileSize,
                                progress = file.progress,
                                isCompleted = file.isCompleted,
                                isError = file.isFailed,
                                showProgress = showProgress,
                                modifier = Modifier
                                    .padding(10.dp),
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