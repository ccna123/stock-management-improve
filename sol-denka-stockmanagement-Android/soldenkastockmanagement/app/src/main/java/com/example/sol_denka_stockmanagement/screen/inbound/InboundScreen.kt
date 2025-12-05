package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.inbound.components.LiterInput
import com.example.sol_denka_stockmanagement.screen.inbound.components.MissRollInput
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState = appViewModel.inputState.collectAsStateWithLifecycle().value
    val expandState = appViewModel.expandState.collectAsStateWithLifecycle().value
    val inboundDetail by scanViewModel.inboundDetail.collectAsStateWithLifecycle()

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
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Inbound.routeId,
        prevScreenNameId = Screen.Inbound.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
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
                onClick = {
//                    appViewModel.onGeneralIntent(
//                        ShareIntent.SaveScanResult(
//                            data = listOf()
//                        )
//                    )
                },
                buttonText = stringResource(R.string.register),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false, // 👈 allow the shadow to bleed outside the box
                        )
                        .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.item_code,inboundDetail?.epc ?: "")
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
                                onChange = { newValue ->
                                    appViewModel.onInputIntent(
                                        InputIntent.ChangeMissRoll(
                                            newValue
                                        )
                                    )
                                },
                                readOnly = true,
                                isDropDown = true,
                                enable = true,
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
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                when (inputState.materialSelectedItem) {
                    SelectTitle.SelectMaterial.displayName -> {}
                    MaterialSelectionItem.MISS_ROLL.displayName -> MissRollInput(
                        thickness = inputState.thickness,
                        rollingMachineInfo = inputState.rollingMachineInfo,
                        stockArea = inputState.location,
                        length = inputState.length,
                        packingStyle = inputState.packingStyle,
                        packingStyleExpanded = expandState.packingStyleExpanded,
                        onThicknessChange = {
                            appViewModel.onInputIntent(
                                InputIntent.ChangeThickness(
                                    it
                                )
                            )
                        },
                        onLengthChange = { appViewModel.onInputIntent(InputIntent.ChangeLength(it)) },
                        onRollingMachineInfoChange = {
                            appViewModel.onInputIntent(
                                InputIntent.ChangeRollingMachineInfo(
                                    it
                                )
                            )
                        },
                        onStockAreaChange = {
                            appViewModel.onInputIntent(
                                InputIntent.ChangeLocation(
                                    it
                                )
                            )
                        },
                        onPackingStyleChange = {
                            appViewModel.onInputIntent(
                                InputIntent.ChangePackingStyle(
                                    it
                                )
                            )
                        },
                        onPackingStyleExpand = { appViewModel.onExpandIntent(ExpandIntent.TogglePackingStyleExpanded) },
                    )

                    MaterialSelectionItem.LITER_CAN.displayName -> {
                        LiterInput(
                            thickness = inputState.thickness,
                            stockArea = inputState.location,
                            lotNo = inputState.lotNo,
                            packingStyle = inputState.packingStyle,
                            packingStyleExpanded = expandState.packingStyleExpanded,
                            onThicknessChange = {
                                appViewModel.onInputIntent(
                                    InputIntent.ChangeThickness(
                                        it
                                    )
                                )
                            },
                            onStockAreaChange = {
                                appViewModel.onInputIntent(
                                    InputIntent.ChangeLocation(
                                        it
                                    )
                                )
                            },
                            onLotNoChange = { appViewModel.onInputIntent(InputIntent.ChangeLotNo(it)) },
                            onPackingStyleChange = {
                                appViewModel.onInputIntent(
                                    InputIntent.ChangePackingStyle(
                                        it
                                    )
                                )
                            },
                            onPackingStyleExpand = { appViewModel.onExpandIntent(ExpandIntent.TogglePackingStyleExpanded) },
                        )
                    }

                    MaterialSelectionItem.PELLET.displayName -> {}
                }
            }
        }
    }
}
