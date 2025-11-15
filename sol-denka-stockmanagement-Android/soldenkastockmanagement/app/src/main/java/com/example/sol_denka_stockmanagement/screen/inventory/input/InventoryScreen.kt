package com.example.sol_denka_stockmanagement.screen.inventory.input

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit
) {

    val errorState by appViewModel.errorState.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            appViewModel.resetState()
        }
    }

    Layout(
        topBarText = stringResource(R.string.inventory),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
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
                shape = RoundedCornerShape(10.dp),
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
        onConfirmProcessedScanDataDialog = {
            onNavigate(Screen.Home)
        },
        onBackArrowClick = {
            onNavigate(Screen.Home)
        }) { paddingValues ->
        InventoryScreenContent(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            appViewModel = appViewModel,
            errorState = errorState,
            expandState = expandState,
            inputState = inputState,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenContent(
    modifier: Modifier,
    appViewModel: AppViewModel,
    errorState: ErrorState,
    expandState: ExpandState,
    inputState: InputState,
) {

    LazyColumn(
        modifier = modifier
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
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    value = if (inputState.stockArea == "保管場所選択") "" else inputState.stockArea,
                    hintText = "保管場所選択",
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    onChange = { newValue ->
                        appViewModel.onInputIntent(
                            InputIntent.UpdateStockArea(
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
                    DropdownMenuItem(
                        text = { Text(text = "保管場所選択") },
                        onClick = {
                            appViewModel.apply {
                                onInputIntent(InputIntent.UpdateStockArea(""))
                                onExpandIntent(ExpandIntent.ToggleStockAreaExpanded)
                            }
                        }
                    )
                    listOf(
                        "保管場所A",
                        "保管場所B",
                        "保管場所C",
                        "保管場所D",
                    ).forEach { stockArea ->
                        DropdownMenuItem(
                            text = { Text(text = stockArea) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(InputIntent.UpdateStockArea(if (stockArea == SelectTitle.SelectStockArea.displayName) "" else stockArea))
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