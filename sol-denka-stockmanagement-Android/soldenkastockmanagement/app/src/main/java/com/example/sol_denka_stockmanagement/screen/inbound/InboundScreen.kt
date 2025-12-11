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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.inbound.components.ItemSearchBar
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
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
    val inboundInputFormResults by appViewModel.inboundInputFormResults.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val locationMaster by appViewModel.locationMaster.collectAsStateWithLifecycle()

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
                            specificGravity = inputState.specificGravity,
                            thickness = inputState.thickness,
                            width = inputState.width,
                            length = inputState.length,
                            quantity = "",
                            winderInfo = inputState.winderInfo,
                            missRollReason = inputState.missRollReason,
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
                                onKeywordChange = { itemName ->
                                    appViewModel.onInputIntent(
                                        InputIntent.SearchKeyWord(
                                            itemName = itemName,
                                        )
                                    )
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.FindItemNameByKeyWord(
                                            itemName
                                        )
                                    )
                                },
                                onSelectItem = { itemName, itemId ->
                                    appViewModel.onInputIntent(
                                        InputIntent.ChangeItem(
                                            itemName = itemName,
                                            itemId = itemId
                                        )
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                inboundInputFormResults
                    .sortedWith(compareBy {
                        if (it.fieldName == "備考") 1 else 0
                    })
                    .forEach { result ->
                        if (result.isVisible) {

                            when (result.controlType) {
                                ControlType.INPUT -> {
                                    InputFieldContainer(
                                        modifier = Modifier
                                            .height(if (result.fieldName == "備考") 200.dp else 60.dp)
                                            .fillMaxWidth(),
                                        value = when (result.fieldName) {
                                            "重量" -> inputState.weight
                                            "長さ" -> inputState.length
                                            "厚み" -> inputState.thickness
                                            "巾" -> inputState.width
                                            "比重" -> inputState.specificGravity
                                            "巻き取り機情報" -> inputState.winderInfo
                                            "ミスロールになった理由" -> inputState.missRollReason
                                            "備考" -> inputState.memo
                                            "Lot No" -> inputState.lotNo
                                            else -> ""
                                        },
                                        label = when (result.fieldName) {
                                            "重量" -> stringResource(R.string.weight)
                                            "長さ" -> stringResource(R.string.length)
                                            "厚み" -> stringResource(R.string.thickness)
                                            "巾" -> stringResource(R.string.width)
                                            "比重" -> stringResource(R.string.specific_gravity)
                                            "巻き取り機情報" -> stringResource(R.string.winderInfo)
                                            "ミスロールになった理由" -> stringResource(R.string.missRollReason)
                                            "備考" -> stringResource(R.string.memo)
                                            "Lot No" -> stringResource(R.string.lot_no)
                                            else -> ""
                                        },
                                        hintText = when (result.fieldName) {
                                            "重量" -> stringResource(R.string.weight_hint)
                                            "長さ" -> stringResource(R.string.length_hint)
                                            "厚み" -> stringResource(R.string.thickness_hint)
                                            "巾" -> stringResource(R.string.width_hint)
                                            "比重" -> stringResource(R.string.specific_gravity_hint)
                                            "巻き取り機情報" -> stringResource(R.string.winderInfo_hint)
                                            "ミスロールになった理由" -> stringResource(R.string.missRollReason_hint)
                                            "備考" -> stringResource(R.string.memo_hint)
                                            "Lot No" -> stringResource(R.string.lot_no_hint)
                                            else -> ""
                                        },
                                        isNumeric = when (result.dataType) {
                                            DataType.TEXT -> false
                                            DataType.NUMBER -> true
                                        },
                                        readOnly = false,
                                        isDropDown = false,
                                        enable = true,
                                        isRequired = result.isRequired,
                                        singleLine = result.fieldName != "備考",
                                        onChange = { newValue ->
                                            when (result.fieldName) {
                                                "重さ" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeWeight(
                                                        newValue
                                                    )
                                                )

                                                "長さ" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeLength(
                                                        newValue
                                                    )
                                                )

                                                "厚み" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeThickness(
                                                        newValue
                                                    )
                                                )

                                                "巾" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeWidth(
                                                        newValue
                                                    )
                                                )

                                                "比重" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeSpecificGravity(
                                                        newValue
                                                    )
                                                )

                                                "巻き取り機情報" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeRollingMachineInfo(newValue)
                                                )

                                                "ミスロールになった理由" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeMissRoll(newValue)
                                                )

                                                "備考" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeMemo(
                                                        newValue
                                                    )
                                                )

                                                "Lot No" -> appViewModel.onInputIntent(
                                                    InputIntent.ChangeLotNo(
                                                        newValue
                                                    )
                                                )
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                }

                                ControlType.DROPDOWN -> {
                                    ExposedDropdownMenuBox(
                                        expanded = expandState.locationExpanded,
                                        onExpandedChange = {
                                            appViewModel.onExpandIntent(
                                                ExpandIntent.ToggleLocationExpanded
                                            )
                                        }
                                    ) {
                                        InputFieldContainer(
                                            modifier = Modifier
                                                .menuAnchor(
                                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                                    enabled = true
                                                )
                                                .fillMaxWidth(),
                                            value = if (inputState.location == SelectTitle.SelectLocation.displayName) "" else inputState.location,
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
                                            expanded = expandState.locationExpanded,
                                            onDismissRequest = {
                                                appViewModel.onExpandIntent(
                                                    ExpandIntent.ToggleLocationExpanded
                                                )
                                            }
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
                                                    text = {
                                                        Text(
                                                            text = location.locationName ?: ""
                                                        )
                                                    },
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
                                    Spacer(modifier = Modifier.height(15.dp))
                                }
                            }
                        }
                    }
            }
        }
    }
}
