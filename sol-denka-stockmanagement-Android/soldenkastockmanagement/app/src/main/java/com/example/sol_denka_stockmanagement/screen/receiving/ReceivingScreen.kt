package com.example.sol_denka_stockmanagement.screen.receiving

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.screen.receiving.components.LiterInput
import com.example.sol_denka_stockmanagement.screen.receiving.components.MissRollInput

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReceivingScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit
) {

    val errorState = appViewModel.errorState.value
    val inputState = appViewModel.inputState.value
    val expandState = appViewModel.expandState.value
    val scannedTag2 by scanViewModel.scannedTag2.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            appViewModel.onGeneralIntent(ShareIntent.ResetState)
        }
    }
    Layout(
        topBarText = stringResource(R.string.receiving),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Receiving.routeId,
        prevScreenNameId = Screen.Receiving.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                shape = RoundedCornerShape(10.dp),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                canClick = inputState.materialSelectedItem != SelectTitle.SelectMaterial.displayName,
                onClick = {},
                buttonText = stringResource(R.string.register_receiving),
            )
        },
        onBackArrowClick = {
            onNavigate(Screen.ReceivingScan)
        }) { paddingValues ->
        ReceivingScreenContent(
            modifier = Modifier.padding(paddingValues),
            appViewModel = appViewModel,
            errorState = errorState,
            expandState = expandState,
            inputState = inputState,
            scannedTag = scannedTag2,
            onUpdateInput = { intent ->
                appViewModel.onInputIntent(intent)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivingScreenContent(
    modifier: Modifier,
    appViewModel: AppViewModel,
    errorState: ErrorState,
    scannedTag: String,
    expandState: ExpandState,
    inputState: InputState,
    onUpdateInput: (InputIntent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(
                    R.string.item_code,
                    scannedTag.takeIf { it.isNotEmpty() } ?: "")
            )
            Spacer(modifier = Modifier.height(20.dp))
            ExposedDropdownMenuBox(
                expanded = expandState.materialSelection,
                onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleMissRollExpanded) }) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    value = if (inputState.materialSelectedItem == SelectTitle.SelectMaterial.displayName) "" else inputState.materialSelectedItem,
                    isNumeric = false,
                    hintText = SelectTitle.SelectMaterial.displayName,
                    shape = RoundedCornerShape(13.dp),
                    onChange = { newValue ->
                        onUpdateInput(InputIntent.ChangeMissRoll(newValue))
                    },
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    onClick = {
                        appViewModel.onExpandIntent(ExpandIntent.ToggleMissRollExpanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expandState.materialSelection,
                    onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleMissRollExpanded) }
                ) {
                    listOf(
                        SelectTitle.SelectMaterial.displayName,
                        MaterialSelectionItem.MISS_ROLL.displayName,
                        MaterialSelectionItem.LITER_CAN.displayName,
                        MaterialSelectionItem.PACKING_STYLE.displayName,
                        MaterialSelectionItem.PAPER_CORE.displayName,
                        MaterialSelectionItem.PELLET.displayName,
                    ).forEach { missRoll ->
                        DropdownMenuItem(
                            text = { Text(text = missRoll) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(InputIntent.ChangeMissRoll(if (missRoll == SelectTitle.SelectMissRoll.displayName) "" else missRoll))
                                    onExpandIntent(ExpandIntent.ToggleMissRollExpanded)
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            when (inputState.materialSelectedItem) {
                SelectTitle.SelectMaterial.displayName -> {}
                MaterialSelectionItem.MISS_ROLL.displayName -> MissRollInput(
                    thickness = inputState.thickness,
                    rollingMachineInfo = inputState.rollingMachineInfo,
                    stockArea = inputState.stockArea,
                    length = inputState.length,
                    packingStyle = inputState.packingStyle,
                    packingStyleExpanded = expandState.packingStyleExpanded,
                    onThicknessChange = { onUpdateInput(InputIntent.ChangeThickness(it)) },
                    onLengthChange = { onUpdateInput(InputIntent.ChangeLength(it)) },
                    onRollingMachineInfoChange = {
                        onUpdateInput(
                            InputIntent.ChangeRollingMachineInfo(
                                it
                            )
                        )
                    },
                    onStockAreaChange = { onUpdateInput(InputIntent.ChangeStockArea(it)) },
                    onPackingStyleChange = { onUpdateInput(InputIntent.ChangePackingStyle(it)) },
                    onPackingStyleExpand = { appViewModel.onExpandIntent(ExpandIntent.TogglePackingStyleExpanded) },
                    stockAreaExpanded = expandState.stockAreaExpanded,
                    onStockAreaExpand = { appViewModel.onExpandIntent(ExpandIntent.ToggleStockAreaExpanded) },
                )

                MaterialSelectionItem.PAPER_CORE.displayName -> {}
                MaterialSelectionItem.LITER_CAN.displayName -> {
                    LiterInput(
                        thickness = inputState.thickness,
                        stockArea = inputState.stockArea,
                        lotNo = inputState.lotNo,
                        packingStyle = inputState.packingStyle,
                        packingStyleExpanded = expandState.packingStyleExpanded,
                        onThicknessChange = { onUpdateInput(InputIntent.ChangeThickness(it)) },
                        onStockAreaChange = { onUpdateInput(InputIntent.ChangeStockArea(it)) },
                        onLotNoChange = { onUpdateInput(InputIntent.ChangeLotNo(it)) },
                        onPackingStyleChange = { onUpdateInput(InputIntent.ChangePackingStyle(it)) },
                        onPackingStyleExpand = { appViewModel.onExpandIntent(ExpandIntent.TogglePackingStyleExpanded) },
                    )
                }
                MaterialSelectionItem.PELLET.displayName -> {}
            }
        }
    }
}