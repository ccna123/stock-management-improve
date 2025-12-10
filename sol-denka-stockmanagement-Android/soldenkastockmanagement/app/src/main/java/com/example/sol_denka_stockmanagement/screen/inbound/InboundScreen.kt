package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.Category
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.inbound.components.ItemSearchBar
import com.example.sol_denka_stockmanagement.screen.inbound.components.LiterInput
import com.example.sol_denka_stockmanagement.screen.inbound.components.MissRollInput
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inboundViewModel: InboundViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val searchResults by appViewModel.searchResults.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

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
                    scope.launch {
                        val result = inboundViewModel.saveInboundToDb(
                            rfidTag = rfidTagList.find { it.epc == lastInboundEpc },
                            weight = inputState.weight,
                            grade = inputState.grade,
                            thickness = inputState.thickness,
                            length = inputState.length,
                            winderInfo = inputState.winderInfo,
                            memo = inputState.memo,
                        )
                        result.exceptionOrNull()?.let { e ->
                            appViewModel.onGeneralIntent(
                                ShareIntent.ShowErrorDialog(
                                    MessageMapper.toMessage(StatusCode.FAILED)
                                )
                            )
                            return@launch
                        }
                        val csvModels = inboundViewModel.generateCsvData(
                            weight = inputState.weight,
                            grade = inputState.grade,
                            specificGravity = "",
                            thickness = inputState.thickness,
                            width = "",
                            length = inputState.length,
                            quantity = "",
                            winderInfo = inputState.winderInfo,
                            missRollReason = "",
                            rfidTag = rfidTagList.find { it.epc == lastInboundEpc },
                        )
                        appViewModel.onGeneralIntent(
                            ShareIntent.SaveScanResult(
                                data = csvModels,
                                direction = CsvHistoryDirection.EXPORT,
                                taskCode = CsvTaskType.IN,
                            )
                        )
                    }
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
                                R.string.item_code, lastInboundEpc ?: ""
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(
                                Category.SUB_MATERIAL.displayName,
                                Category.SUB_RAW_MATERIAL.displayName,
                                Category.NON_STANDARD_ITEM.displayName,
                            ).forEachIndexed { index, category ->

                                val isSelected = index == generalState.selectedChipIndex

                                // --- ANIMATION COLORS ---
                                val bgColor by animateColorAsState(
                                    targetValue = if (isSelected) brightGreenSecondary else Color.White,
                                    label = "chip-bg"
                                )
                                val labelColor by animateColorAsState(
                                    targetValue = if (isSelected) Color.White else Color.Black,
                                    label = "chip-label"
                                )
                                val iconTint by animateColorAsState(
                                    targetValue = if (isSelected) Color.White else Color.Black,
                                    label = "chip-icon"
                                )
                                val borderColor by animateColorAsState(
                                    targetValue = if (isSelected) Color.Transparent else brightAzure,
                                    label = "chip-border"
                                )

                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        appViewModel.onGeneralIntent(
                                            ShareIntent.SelectChipIndex(
                                                index
                                            )
                                        )
                                    },
                                    label = { Text(text = category, color = labelColor) },
                                    leadingIcon = {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = iconTint,
                                            )
                                        }
                                    },
                                    enabled = true,
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = bgColor,
                                        disabledContainerColor = bgColor,
                                        labelColor = labelColor,
                                        iconColor = iconTint,
                                        selectedContainerColor = bgColor,
                                        selectedLabelColor = labelColor,
                                        selectedLeadingIconColor = iconTint,
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = borderColor,
                                        borderWidth = 1.dp,
                                        enabled = true,
                                        selected = isSelected
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp, max = 300.dp)
                        ) {
                            ItemSearchBar(
                                keyword = inputState.item,
                                results = searchResults,
                                onKeywordChange = {
                                    appViewModel.onInputIntent(InputIntent.ChangeItem(it))
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.FindItemNameByKeyWord(
                                            it
                                        )
                                    )
                                },
                                onSelectItem = {
                                    appViewModel.onInputIntent(InputIntent.ChangeItem(it))
                                }
                            )
                        }
//                        InputFieldContainer(
//                            modifier = Modifier.fillMaxWidth(),
//                            value = inputState.item,
//                            label = stringResource(R.string.item),
//                            hintText = stringResource(R.string.item_hint),
//                            isNumeric = false,
//                            readOnly = false,
//                            isDropDown = false,
//                            enable = true,
//                            onChange = { newValue ->
//                                appViewModel.apply {
//                                    onInputIntent(InputIntent.ChangeItem(newValue))
//                                    onGeneralIntent(ShareIntent.FindItemNameByKeyWord(newValue))
//                                }
//                            }
//                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                when (inputState.materialSelectedItem) {
                    SelectTitle.SelectMaterial.displayName -> {}
                    MaterialSelectionItem.MISS_ROLL.displayName -> MissRollInput(
                        thickness = inputState.thickness,
                        rollingMachineInfo = inputState.winderInfo,
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
                            location = inputState.location,
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
                            onLocationChange = {
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
