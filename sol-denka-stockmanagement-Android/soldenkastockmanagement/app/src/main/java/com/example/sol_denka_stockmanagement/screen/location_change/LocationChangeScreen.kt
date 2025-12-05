package com.example.sol_denka_stockmanagement.screen.location_change

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.example.sol_denka_stockmanagement.constant.SelectTitle
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LocationChangeScreen(
    appViewModel: AppViewModel,
    locationChangeViewModel: LocationChangeViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit
) {

    val expandState = appViewModel.expandState.collectAsStateWithLifecycle()
    val inputState = appViewModel.inputState.collectAsStateWithLifecycle()
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()
    val checkedMap by appViewModel.perTagChecked.collectAsStateWithLifecycle()
    val locationMaster by appViewModel.locationMaster.collectAsStateWithLifecycle()
    val locationChangePreview by locationChangeViewModel.locationChangeList.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val selectedEpc = checkedMap.filter { it.value }.map { it.key }
        locationChangeViewModel.getTagDetailForLocationChange(selectedEpc)
    }

    Layout(
        topBarText = stringResource(R.string.storage_area_change),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Outbound.routeId,
        prevScreenNameId = Screen.Outbound.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .shadow(
                        elevation = 13.dp,
                        clip = true,
                        ambientColor = Color.Gray.copy(alpha = 0.5f),
                        spotColor = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                buttonText = stringResource(R.string.storage_area_change),
                canClick = inputState.value.location.isNotEmpty(),
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
                        locationChangeViewModel.saveLocationChangeToDb(
                            memo = inputState.value.memo,
                            newLocation = inputState.value.location
                        )
                        val csvModels =
                            locationChangeViewModel.generateCsvData(
                                memo = inputState.value.memo,
                                newLocation = inputState.value.location
                            )
                        appViewModel.onGeneralIntent(
                            ShareIntent.SaveScanResult(
                                data = csvModels,
                                direction = CsvHistoryDirection.EXPORT,
                                taskCode = CsvTaskType.LOCATION_CHANGE
                            )
                        )
                    }
                },
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
            Text(text = stringResource(R.string.planned_register_item_number, selectedCount))
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
                            stringResource(R.string.storage_area)
                        ),
                        scanResult = locationChangePreview.map { tag ->
                            ScanResultRowModel(
                                itemName = tag.itemName ?: "-",
                                itemCode = tag.epc,
                                lastColumn = tag.location ?: "-"
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandState.value.locationExpanded,
                        onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded) }) {
                        InputFieldContainer(
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            value = if (inputState.value.location == SelectTitle.SelectLocation.displayName) "" else inputState.value.location,
                            hintText = SelectTitle.SelectLocation.displayName,
                            isNumeric = false,
                            onChange = { newValue ->
                                appViewModel.onInputIntent(
                                    InputIntent.ChangeLocation(
                                        newValue
                                    )
                                )
                            },
                            readOnly = true,
                            isDropDown = true,
                            enable = true,
                            onEnterPressed = {}
                        )
                        ExposedDropdownMenu(
                            expanded = expandState.value.locationExpanded,
                            onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = SelectTitle.SelectLocation.displayName) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(InputIntent.ChangeLocation(""))
                                        onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                    }
                                }
                            )
                            locationMaster.forEach { location ->
                                DropdownMenuItem(
                                    text = { Text(text = location.locationName ?: "") },
                                    onClick = {
                                        appViewModel.apply {
                                            onInputIntent(
                                                InputIntent.ChangeLocation(
                                                    if (location.locationName == SelectTitle.SelectLocation.displayName) "" else location.locationName
                                                        ?: ""
                                                )
                                            )
                                            onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                        }
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
                        value = inputState.value.memo,
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
                                InputIntent.ChangeRemark(
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