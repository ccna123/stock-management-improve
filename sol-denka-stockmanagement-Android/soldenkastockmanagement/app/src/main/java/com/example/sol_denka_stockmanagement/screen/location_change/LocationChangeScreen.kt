package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LocationChangeScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    locationChangeViewModel: LocationChangeViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit
) {

    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val locationMaster by appViewModel.locationMaster.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Layout(
        topBarText = stringResource(R.string.storage_area_change),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
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
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .weight(1f)
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    buttonText = stringResource(R.string.storage_area_change),
                    canClick = inputState.location != null,
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
                            val result = locationChangeViewModel.saveLocationChangeToDb(
                                memo = inputState.memo,
                                locationId = inputState.location?.locationId ?: 0,
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
                                locationChangeViewModel.generateCsvData(
                                    memo = inputState.memo,
                                    locationId = inputState.location?.locationId ?: 0,
                                    rfidTagList = rfidTagList.filter { it.newFields.isChecked }
                                )
                            val saveResult = appViewModel.saveScanResultToCsv(
                                data = csvModels,
                                direction = CsvHistoryDirection.EXPORT,
                                taskCode = CsvTaskType.LOCATION_CHANGE,
                            )
                            if (saveResult) {
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
                            } else {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ShowDialog(
                                        type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                                        message = MessageMapper.toMessage(StatusCode.EXPORT_OK)
                                    )
                                )
                            }
                        }
                    },
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
                    rfidTagList.count { it.newFields.isChecked })
            )
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn(
                modifier = Modifier
                    .imePadding()
            ) {
                item {
                    ScanResultTable(
                        tableHeader = listOf(
                            stringResource(R.string.item_name_title),
                            stringResource(R.string.item_code_title),
                            stringResource(R.string.location)
                        ),
                        scanResult = rfidTagList.filter { it.newFields.isChecked }.map { tag ->
                            ScanResultRowModel(
                                itemName = rfidTagList.find { it.epc == tag.epc }?.epc ?: "-",
                                itemCode = rfidTagList.find { it.epc == tag.epc }?.newFields?.itemCode
                                    ?: "-",
                                lastColumn = rfidTagList.find { it.epc == tag.epc }?.newFields?.location
                                    ?: "-",
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandState.locationExpanded,
                        onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded) }) {
                        InputFieldContainer(
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            value = if (inputState.location?.locationName == SelectTitle.SelectLocation.displayName) "" else inputState.location?.locationName
                                ?: "",
                            hintText = SelectTitle.SelectLocation.displayName,
                            isNumeric = false,
                            label = SelectTitle.SelectLocation.displayName,
                            onChange = {},
                            readOnly = true,
                            isDropDown = true,
                            enable = true,
                            isRequired = true,
                            onEnterPressed = {}
                        )
                        ExposedDropdownMenu(
                            expanded = expandState.locationExpanded,
                            onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(SelectTitle.SelectLocation.displayName) },
                                onClick = {
                                    appViewModel.onInputIntent(InputIntent.ChangeLocation(null))
                                    appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                }
                            )
                            locationMaster.forEach { location ->
                                DropdownMenuItem(
                                    text = { Text(location.locationName) },
                                    onClick = {
                                        appViewModel.onInputIntent(
                                            InputIntent.ChangeLocation(
                                                location
                                            )
                                        )
                                        appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                    }
                                )
                            }
                        }
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
                        readOnly = false,
                        isDropDown = false,
                        enable = true,
                        singleLine = false,
                        onChange = { newValue ->
                            val filteredValue = newValue.trimStart().filter { char ->
                                (char.isLetterOrDigit() && char.toString()
                                    .toByteArray().size == 1) || char == '-'
                            }
                            appViewModel.onInputIntent(
                                InputIntent.ChangeMemo(
                                    filteredValue
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}