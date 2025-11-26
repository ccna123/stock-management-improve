package com.example.sol_denka_stockmanagement.screen.inventory.input

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StockAreaItem
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState = appViewModel.inputState.collectAsStateWithLifecycle().value
    val expandState = appViewModel.expandState.collectAsStateWithLifecycle().value
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle().value
    val showClearTagConfirmDialog = appViewModel.showClearTagConfirmDialog.value

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            appViewModel.onGeneralIntent(ShareIntent.ResetState)
        }
    }
    ConfirmDialog(
        showDialog = showClearTagConfirmDialog,
        dialogTitle = stringResource(R.string.clear_processed_tag_dialog),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    onClick = {
                        scanViewModel.clearProcessedTag()
                        appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
                        onGoBack()
                    }
                )
            },
            {
                ButtonContainer(
                    containerColor = Color.Red,
                    buttonText = stringResource(R.string.no),
                    onClick = {
                        appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
                    }
                )
            }
        )
    )

    Layout(
        topBarText = stringResource(R.string.inventory),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Inventory.routeId,
        prevScreenNameId = Screen.Inventory.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.scan),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                canClick = inputState.stockArea.isNotEmpty(),
                onClick = {
                    onNavigate(Screen.InventoryScan(Screen.Inventory.routeId))
                },
                buttonText = stringResource(R.string.inventory_start),
            )
        },
        onBackArrowClick = {
            if (rfidTagList.count { it.newField.tagStatus == TagStatus.PROCESSED } > 0) {
                appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
            } else {
                onGoBack()
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(16.dp)
                .imePadding()
        ) {
            item {
                Text(text = "棚卸を行う保管場所を選択")
                Spacer(modifier = Modifier.height(10.dp))
                ExposedDropdownMenuBox(
                    expanded = expandState.stockAreaExpanded,
                    onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleStockAreaExpanded) }) {
                    InputFieldContainer(
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth(),
                        value = if (inputState.stockArea == StockAreaItem.SELECTION_TITLE.displayName) "" else inputState.stockArea,
                        hintText = StockAreaItem.SELECTION_TITLE.displayName,
                        isNumeric = false,
                        onChange = { newValue ->
                            appViewModel.onInputIntent(
                                InputIntent.ChangeStockArea(
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
                        expanded = expandState.stockAreaExpanded,
                        onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleStockAreaExpanded) }
                    ) {
                        listOf(
                            StockAreaItem.SELECTION_TITLE.displayName,
                            StockAreaItem.STOCK_AREA1.displayName,
                            StockAreaItem.STOCK_AREA2.displayName,
                            StockAreaItem.STOCK_AREA3.displayName,
                            StockAreaItem.STOCK_AREA4.displayName,
                            StockAreaItem.STOCK_AREA5.displayName,
                        ).forEach { stockArea ->
                            DropdownMenuItem(
                                text = { Text(text = stockArea) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(InputIntent.ChangeStockArea(if (stockArea == SelectTitle.SelectStockArea.displayName) "" else stockArea))
                                        onExpandIntent(ExpandIntent.ToggleStockAreaExpanded)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}