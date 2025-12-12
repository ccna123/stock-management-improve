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
import androidx.compose.foundation.layout.heightIn
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
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.PackingStyleItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.inbound.components.ItemSearchBar
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
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
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val searchResults by appViewModel.searchResults.collectAsStateWithLifecycle()
    val inboundInputFormResults by appViewModel.inboundInputFormResults.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val locationMaster by appViewModel.locationMaster.collectAsStateWithLifecycle()
    val itemCategoryMaster by appViewModel.itemCategoryMaster.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()

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
                            specificGravity = inputState.specificGravity,
                            width = inputState.width,
                            quantity = "",
                            missRollReason = inputState.missRollReason,
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
                        val saveResult = appViewModel.saveScanResultToCsv(
                            data = csvModels,
                            direction = CsvHistoryDirection.EXPORT,
                            taskCode = CsvTaskType.IN,
                        )
                        saveResult
                            .onSuccess {
                                // SAVE CSV success
                                // Now check network and send SFTP
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
                            }
                            .onFailure { throwable ->
                                val code = StatusCode.valueOf(throwable.message!!)
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ShowDialog(
                                        type = DialogType.ERROR,
                                        message = MessageMapper.toMessage(code)
                                    )
                                )
                            }
                    }
                },
                buttonText = stringResource(R.string.register),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    ExposedDropdownMenuBox(
                        expanded = expandState.categoryExpanded,
                        onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleCategoryExpanded) }) {
                        InputFieldContainer(
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            value = if (inputState.category == SelectTitle.SelectCategory.displayName) "" else inputState.category,
                            hintText = SelectTitle.SelectCategory.displayName,
                            isNumeric = false,
                            onChange = { newValue ->
                                appViewModel.onInputIntent(
                                    InputIntent.ChangeCategory(
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
                            expanded = expandState.categoryExpanded,
                            onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleCategoryExpanded) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = SelectTitle.SelectLocation.displayName) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(InputIntent.ChangeCategory(""))
                                        onExpandIntent(ExpandIntent.ToggleCategoryExpanded)
                                    }
                                }
                            )
                            itemCategoryMaster.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category.itemCategoryName) },
                                    onClick = {
                                        appViewModel.apply {
                                            onInputIntent(
                                                InputIntent.ChangeCategory(
                                                    if (category.itemCategoryName == SelectTitle.SelectCategory.displayName) "" else category.itemCategoryName
                                                )
                                            )
                                            onExpandIntent(ExpandIntent.ToggleCategoryExpanded)
                                        }
                                    }
                                )
                            }
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
            LazyColumn {
                item {

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
                                                    "重量" -> appViewModel.onInputIntent(
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
                                                        InputIntent.ChangeWinderInfo(newValue)
                                                    )

                                                    "ミスロールになった理由" -> appViewModel.onInputIntent(
                                                        InputIntent.ChangeMissRollReason(newValue)
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
                                            expanded = when (result.fieldName) {
                                                "保管場所" -> expandState.locationExpanded
                                                "荷姿" -> expandState.packingStyleExpanded
                                                else -> false
                                            },
                                            onExpandedChange = {
                                                when (result.fieldName) {
                                                    "保管場所" -> {
                                                        appViewModel.onExpandIntent(
                                                            ExpandIntent.ToggleLocationExpanded
                                                        )
                                                    }

                                                    "荷姿" -> {
                                                        appViewModel.onExpandIntent(
                                                            ExpandIntent.TogglePackingStyleExpanded
                                                        )
                                                    }

                                                    else -> false
                                                }

                                            }
                                        ) {
                                            InputFieldContainer(
                                                modifier = Modifier
                                                    .menuAnchor(
                                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                                        enabled = true
                                                    )
                                                    .fillMaxWidth(),
                                                value = when (result.fieldName) {
                                                    "保管場所" -> if (inputState.location == SelectTitle.SelectLocation.displayName) "" else inputState.location
                                                    "荷姿" -> if (inputState.packingStyle == SelectTitle.SelectPackingStyle.displayName) "" else inputState.packingStyle
                                                    else -> ""
                                                },
                                                hintText = when (result.fieldName) {
                                                    "保管場所" -> SelectTitle.SelectLocation.displayName
                                                    "荷姿" -> SelectTitle.SelectPackingStyle.displayName
                                                    else -> ""
                                                },
                                                isNumeric = false,
                                                onChange = { newValue ->
                                                    when (result.fieldName) {
                                                        "保管場所" -> appViewModel.onInputIntent(
                                                            InputIntent.ChangeLocation(newValue)
                                                        )

                                                        "荷姿" -> appViewModel.onInputIntent(
                                                            InputIntent.ChangePackingStyle(
                                                                newValue
                                                            )
                                                        )

                                                        else -> ""
                                                    }
                                                },
                                                readOnly = true,
                                                isDropDown = true,
                                                enable = true,
                                                onEnterPressed = {}
                                            )
                                            ExposedDropdownMenu(
                                                expanded = when (result.fieldName) {
                                                    "保管場所" -> expandState.locationExpanded
                                                    "荷姿" -> expandState.packingStyleExpanded
                                                    else -> false
                                                },
                                                onDismissRequest = {
                                                    when (result.fieldName) {
                                                        "保管場所" -> appViewModel.onExpandIntent(
                                                            ExpandIntent.ToggleLocationExpanded
                                                        )

                                                        "荷姿" -> appViewModel.onExpandIntent(
                                                            ExpandIntent.TogglePackingStyleExpanded
                                                        )

                                                        else -> ""
                                                    }
                                                }
                                            ) {
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = when (result.fieldName) {
                                                                "保管場所" -> SelectTitle.SelectLocation.displayName
                                                                "荷姿" -> SelectTitle.SelectPackingStyle.displayName
                                                                else -> ""
                                                            }
                                                        )
                                                    },
                                                    onClick = {
                                                        when (result.fieldName) {
                                                            "保管場所" -> {
                                                                appViewModel.apply {
                                                                    onInputIntent(
                                                                        InputIntent.ChangeLocation(
                                                                            ""
                                                                        )
                                                                    )
                                                                    onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                                                }
                                                            }

                                                            "荷姿" -> {
                                                                appViewModel.apply {
                                                                    onInputIntent(
                                                                        InputIntent.ChangePackingStyle(
                                                                            ""
                                                                        )
                                                                    )
                                                                    onExpandIntent(ExpandIntent.TogglePackingStyleExpanded)
                                                                }
                                                            }

                                                            else -> ""
                                                        }
                                                    }
                                                )
                                                when (result.fieldName) {
                                                    "保管場所" -> {
                                                        locationMaster.forEach { location ->
                                                            DropdownMenuItem(
                                                                text = {
                                                                    location.locationName?.let {
                                                                        Text(
                                                                            text = it
                                                                        )
                                                                    }
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

                                                    "荷姿" -> {
                                                        listOf(
                                                            PackingStyleItem.PAPER_BAG_25KG.displayName,
                                                            PackingStyleItem.FLEXIBLE_CONTAINER_1T.displayName
                                                        ).forEach { packingStyle ->
                                                            DropdownMenuItem(
                                                                text = {
                                                                    Text(text = packingStyle)
                                                                },
                                                                onClick = {
                                                                    appViewModel.apply {
                                                                        onInputIntent(
                                                                            InputIntent.ChangePackingStyle(
                                                                                if (inputState.packingStyle == SelectTitle.SelectPackingStyle.displayName) "" else packingStyle
                                                                            )
                                                                        )
                                                                        onExpandIntent(ExpandIntent.TogglePackingStyleExpanded)
                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }
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
}
