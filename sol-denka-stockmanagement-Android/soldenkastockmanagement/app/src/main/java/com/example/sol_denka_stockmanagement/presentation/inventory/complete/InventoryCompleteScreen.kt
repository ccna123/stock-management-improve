package com.example.sol_denka_stockmanagement.presentation.inventory.complete

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.InventoryScanResult
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.domain.model.inventory.InventoryCompleteModel
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.presentation.inventory.input.InventoryIntent
import com.example.sol_denka_stockmanagement.presentation.inventory.input.InventoryViewModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.CardContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.primaryRed
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryCompleteScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inventoryViewModel: InventoryViewModel,
    inventoryCompleteViewModel: InventoryCompleteViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {
    val uiState by inventoryCompleteViewModel.uiState.collectAsStateWithLifecycle()
    val inventoryUiState by inventoryViewModel.uiState.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
        inventoryCompleteViewModel.onIntent(
            InventoryCompleteIntent.ComputeResult(
                rfidTagList = rfidTagList,
                locationName = inventoryUiState.location?.locationName ?: ""
            )
        )
    }

    LaunchedEffect(uiState.event) {
        when (uiState.event) {
            is InventoryCompleteEvent.SaveDbFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.ERROR,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_DB_FAILED)
                    )
                )
            is InventoryCompleteEvent.SaveCsvSuccess ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                        message = MessageMapper.toMessage(StatusCode.SAVE_CSV_SUCCESS_FAILED_SFTP)
                    )
                )
            is InventoryCompleteEvent.SaveCsvFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_FAILED,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_CSV_FAILED)
                    )
                )
            null -> Unit
        }
        if (uiState.event != null) {
            inventoryCompleteViewModel.onIntent(InventoryCompleteIntent.EventConsumed)
        }
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
        hasBottomBar = true,
        retrySaveDb = {
            inventoryCompleteViewModel.onIntent(InventoryCompleteIntent.Retry)
        },
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
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    val now = generateIso8601JstTimestamp()
                    inventoryCompleteViewModel.onIntent(
                        InventoryCompleteIntent.Execute(
                            memo = inventoryUiState.memo,
                            locationId = inventoryUiState.location?.locationId ?: 0,
                            scannedAt = now,
                            executedAt = now,
                            rfidTagList = rfidTagList.filter {
                                it.newFields.tagScanStatus == TagScanStatus.PROCESSED
                            }
                        )
                    )
                },
                buttonText = stringResource(R.string.finish_inventory),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 20.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.csv_export_title))
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(
                modifier = Modifier.imePadding()
            ) {
                item {
                    CardContainer {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            inventoryStatusList.forEach { item ->
                                val count = when (item.status) {
                                    InventoryScanResult.OK -> uiState.okCount
                                    InventoryScanResult.SHORTAGE -> uiState.shortageCount
                                    InventoryScanResult.OVERLOAD -> uiState.overCount
                                    InventoryScanResult.WRONG_LOCATION -> uiState.wrongLocationCount
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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
                        value = inventoryUiState.memo,
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
                            inventoryViewModel.onIntent(InventoryIntent.MemoChanged(filteredValue))
                        }
                    )
                }
            }
        }
    }
}