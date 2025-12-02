package com.example.sol_denka_stockmanagement.screen.csv

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.helper.ProcessResult
import com.example.sol_denka_stockmanagement.intent.CsvIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.csv.components.SingleCsvFile
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.AppDialog
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CsvImportScreen(
    csvViewModel: CsvViewModel,
    appViewModel: AppViewModel,
    onGoBack: () -> Unit
) {
    val csvState by csvViewModel.csvState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val generalState = appViewModel.generalState.collectAsState().value
    val importFileSelectedIndex = csvViewModel.importFileSelectedIndex.collectAsState().value
    val importFileSelectedName = csvViewModel.importFileSelectedName.collectAsState().value
    val csvFiles by csvViewModel.csvFiles.collectAsStateWithLifecycle()
    val isImporting by csvViewModel.isImporting.collectAsStateWithLifecycle()
    val importProgress by csvViewModel.importProgress.collectAsStateWithLifecycle()
    val showProcessResultDialog by csvViewModel.showProcessResultDialog.collectAsStateWithLifecycle()
    val processResultMessage by csvViewModel.processResultMessage.collectAsStateWithLifecycle()
    val importResultStatus by csvViewModel.importResultStatus.collectAsStateWithLifecycle()

    LaunchedEffect(csvState.csvType) {
        when (csvState.csvType) {
            in listOf(
                CsvType.LedgerMaster.displayName,
                CsvType.LocationMaster.displayName,
                CsvType.ItemTypeMaster.displayName,
                CsvType.TagMaster.displayName,
            ) -> {
                csvViewModel.apply {
                    fetchCsvFiles()
                    toggleProgressVisibility(false)
                    onCsvIntent(CsvIntent.ResetFileSelect)
                }
            }

            else -> csvViewModel.clearCsvList()
        }
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(
                ShareIntent.ToggleNetworkDialog(false)
            )
        })
    }

//    if (isImporting) {
//        AppDialog {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = if (importProgress < 1f) "CSVファイル取り込み中" else "CSVファイル取り込みに成功しました",
//                    textAlign = TextAlign.Center,
//                    color = if (importProgress < 1f) Color.Black else brightGreenPrimary
//                )
//                Spacer(Modifier.height(12.dp))
//                LinearProgressIndicator(
//                    progress = {
//                        importProgress
//                    },
//                    modifier = Modifier
//                        .height(6.dp),
//                )
//                Text("${(importProgress * 100).toInt()}%")
//            }
//        }
//    }

    if (showProcessResultDialog) {
        AppDialog {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = processResultMessage ?: "",
                    textAlign = TextAlign.Center,
                    color = when (importResultStatus) {
                        is ProcessResult.Failure -> Color.Red
                        is ProcessResult.Success -> brightGreenPrimary
                        null -> Color.Unspecified
                    }
                )
                Spacer(Modifier.height(12.dp))
                ButtonContainer(
                    containerColor = when (importResultStatus) {
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
            csvViewModel.resetState()
        }
    }

    Layout(
        topBarText = "${Screen.fromRouteId(Screen.CsvImport.routeId)?.displayName}",
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.CsvImport.routeId,
        prevScreenNameId = Screen.CsvImport.routeId,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                buttonTextSize = 20,
                buttonText = stringResource(R.string.import_file),
                canClick = csvState.csvType.isNotEmpty() && csvFiles.isNotEmpty() && importFileSelectedIndex != -1,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.file_import),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                onClick = {
                    scope.launch {
                        csvViewModel.importMaster()
                    }
//                    if (appViewModel.isNetworkConnected.value.not()) {
//                        appViewModel.onGeneralIntent(
//                            ShareIntent.ToggleNetworkDialog(true)
//                        )
//                    } else {
//                        scope.launch {
////                            csvViewModel.downloadCsvFromSftp(context)
//                            csvViewModel.importMaster()
//                        }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false, // 👈 allow the shadow to bleed outside the box
                    )
                    .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    InputContainer(
                        title = stringResource(R.string.csv_type_selection),
                        isRequired = true,
                        children = {
                            ExposedDropdownMenuBox(
                                expanded = csvState.csvTypeExpanded,
                                onExpandedChange = { csvViewModel.toggleCsvTypeExpanded() }) {
                                InputFieldContainer(
                                    modifier = Modifier
                                        .menuAnchor(
                                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                            enabled = true
                                        )
                                        .fillMaxWidth(),
                                    value = if (csvState.csvType == SelectTitle.SelectCsvType.displayName) "" else csvState.csvType,
                                    hintText = SelectTitle.SelectCsvType.displayName,
                                    isNumeric = false,
                                    shape = RoundedCornerShape(13.dp),
                                    onChange = { csvViewModel.updateState { copy(csvType = it) } },
                                    readOnly = true,
                                    isDropDown = true,
                                    enable = true,
                                )
                                ExposedDropdownMenu(
                                    expanded = csvState.csvTypeExpanded,
                                    onDismissRequest = { csvViewModel.toggleCsvTypeExpanded() }
                                ) {
                                    listOf(
                                        SelectTitle.SelectCsvType.displayName,
                                        CsvType.LedgerMaster.displayName,
                                        CsvType.LocationMaster.displayName,
                                        CsvType.ItemTypeMaster.displayName,
                                        CsvType.TagMaster.displayName,
                                    ).forEach { csvType ->
                                        DropdownMenuItem(
                                            text = { Text(text = csvType) },
                                            onClick = {
                                                csvViewModel.updateState {
                                                    copy(
                                                        csvType = if (csvType == SelectTitle.SelectCsvType.displayName) "" else csvType
                                                    )
                                                }
                                                csvViewModel.toggleCsvTypeExpanded()
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
                    if (csvState.csvType == "") {
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
                                isSelected = importFileSelectedIndex == index,
                                modifier = Modifier
                                    .padding(10.dp),
                                onChoose = {
                                    csvViewModel.onCsvIntent(CsvIntent.ToggleFileSelect(fileIndex = index, fileName = file.fileName))
                                }
                            )
                        }
                            ?: Text(
                                color = Color.Red,
                                text = stringResource(R.string.no_csv_file_found)
                            )
                    }
                }
            }
        }
    }
}