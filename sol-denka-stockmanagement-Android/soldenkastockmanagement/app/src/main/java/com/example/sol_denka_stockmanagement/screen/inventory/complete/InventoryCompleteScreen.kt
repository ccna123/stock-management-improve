package com.example.sol_denka_stockmanagement.screen.inventory.complete

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.InventoryScanResult
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.inventory.InventoryCompleteModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.ui.theme.primaryRed
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryCompleteScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inventoryCompleteViewModel: InventoryCompleteViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val wrongLocationCount by inventoryCompleteViewModel.wrongLocationCount.collectAsStateWithLifecycle()
    val shortageCount by inventoryCompleteViewModel.shortageCount.collectAsStateWithLifecycle()
    val overCount by inventoryCompleteViewModel.overCount.collectAsStateWithLifecycle()
    val okCount by inventoryCompleteViewModel.okCount.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    LaunchedEffect(rfidTagList) {
        inventoryCompleteViewModel.computeResult(
            rfidTagList = rfidTagList.filter { it.newFields.hasLeger },
            locationName = inputState.location?.locationName ?: ""
        )
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(
                ShareIntent.ToggleNetworkDialog(false)
            )
        })
    }
    val inventoryStatusList = listOf(
        InventoryCompleteModel(
            status = InventoryScanResult.OK,
            icon = R.drawable.scan_ok,
            color = brightGreenPrimary,
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.SHORTAGE,
            icon = R.drawable.scan_shortage,
            color = brightOrange,
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.OVERLOAD,
            icon = R.drawable.scan_overload,
            color = primaryRed,
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.WRONG_LOCATION,
            icon = R.drawable.scan_wrong_location,
            color = deepBlueSky,
        )
    )

    Layout(
        topBarText = Screen.InventoryComplete.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.InventoryComplete.routeId,
        prevScreenNameId = Screen.InventoryComplete.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    scope.launch {
                        val result = inventoryCompleteViewModel.saveInventoryResultToDb(
                            memo = inputState.memo,
                            locationId = inputState.location?.locationId ?: 0,
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
                            inventoryCompleteViewModel.generateCsvData(
                                memo = inputState.memo,
                                locationId = inputState.location?.locationId ?: 0,
                            )
                        val saveResult = appViewModel.saveScanResultToCsv(
                            data = csvModels,
                            direction = CsvHistoryDirection.EXPORT,
                            taskCode = CsvTaskType.INVENTORY,
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
                buttonText = stringResource(R.string.finish_inventory),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 20.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.csv_export_title))
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(
                modifier = Modifier
                    .imePadding()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(12.dp),
                                clip = false, // 👈 allow the shadow to bleed outside the box
                            )
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(10.dp)),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            inventoryStatusList.forEach { item ->
                                val count = when (item.status) {
                                    InventoryScanResult.OK -> okCount
                                    InventoryScanResult.SHORTAGE -> shortageCount
                                    InventoryScanResult.OVERLOAD -> overCount
                                    InventoryScanResult.WRONG_LOCATION -> wrongLocationCount
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(item.icon),
                                            contentDescription = null,
                                            tint = item.color,
                                            modifier = Modifier.size(23.dp)
                                        )
                                        Text(text = item.status.displayName, fontSize = 18.sp)
                                    }
                                    Text(
                                        text = "$count 件",
                                        fontSize = 18.sp,
                                        color = item.color
                                    )
                                }
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