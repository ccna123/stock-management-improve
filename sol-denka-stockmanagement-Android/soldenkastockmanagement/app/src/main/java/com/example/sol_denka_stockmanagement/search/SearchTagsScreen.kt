package com.example.sol_denka_stockmanagement.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.helper.TagDistanceCalculate
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.search.components.SingleRfidRow
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.deepOceanBlue
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.share.RadioPowerDialog
import com.example.sol_denka_stockmanagement.state.GeneralState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchTagsScreen(
    appViewModel: AppViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    scanViewModel: ScanViewModel,
    prevScreenNameId: String,
    onNavigate: (Screen) -> Unit,
) {
    val generalState = appViewModel.generalState.value
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val readerSettingState by readerSettingViewModel.readerSettingState.collectAsStateWithLifecycle()
    var showRadioPowerDialog by remember { mutableStateOf(false) }
    val isPerformingInventory by scanViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Log.e("TSS", "prevScreenNameId: $prevScreenNameId", )


    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, screen = Screen.SearchTagsScreen(""))
        appViewModel.onGeneralIntent(ShareIntent.ClearFoundTag)
    }

    RadioPowerDialog(
        showDialog = showRadioPowerDialog,
        readerSettingState = readerSettingState,
        onMinPower = {
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        0
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(0))
            }
        },
        onMaxPower = {
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        30
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(30))
            }
        },
        onChangeSlider = { intValue ->
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        intValue
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(intValue))
            }
        },
        onOk = {
            readerSettingViewModel.apply {
                onIntent(ReaderSettingIntent.ChangeRadioPower(readerSettingState.radioPower))
                setRadioPower(readerSettingState.radioPower)
            }
            showRadioPowerDialog = false
        },
        onDismiss = {
            showRadioPowerDialog = false
        }
    )

    Layout(
        topBarText = "${Screen.fromRouteId(prevScreenNameId)?.displayName} (探索)",
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.SearchTagsScreen("").routeId,
        prevScreenNameId = prevScreenNameId,
        hasBottomBar = true,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        readerSettingViewModel = readerSettingViewModel,
        topBarButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                ButtonContainer(
                    modifier = Modifier,
                    buttonHeight = 35.dp,
                    containerColor = orange,
                    shape = RoundedCornerShape(5.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    canClick = generalState.foundTags.isNotEmpty(),
                    textColor = Color.White,
                    buttonText = stringResource(R.string.finish_search),
                    onClick = {
                        generalState.foundTags.forEach { foundTag ->
                            scanViewModel.updateTagStatus(
                                epc = foundTag.epc,
                                newStatus = TagStatus.PROCESSED
                            )
                        }
                        onNavigate(Screen.InventoryScan(Screen.SearchTagsScreen("").routeId))
                    },
                )
                Box {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                onClick = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleDropDown(true)
                                    )
                                }
                            )
                    )
                    DropdownMenu(
                        expanded = generalState.showDropDown,
                        onDismissRequest = {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ToggleDropDown(false)
                            )
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.setting_rfid_power)) },
                            onClick = {
                                showRadioPowerDialog = true
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ToggleDropDown(false)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.clear)) },
                            onClick = {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ToggleDropDown(false)
                                )
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ClearFoundTag
                                )
                            }
                        )
                    }
                }
            }
        },
        bottomButton = {
            ButtonContainer(
                buttonText = if (isPerformingInventory) stringResource(R.string.scan_stop) else stringResource(
                    R.string.scan_start
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.shadow(
                    elevation = 13.dp,
                    clip = true,
                    ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.scanner),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                containerColor = if (isPerformingInventory) Color.Red else brightAzure,
                onClick = {
                    scope.launch {
                        if (isPerformingInventory) scanViewModel.stopInventory() else scanViewModel.startInventory()
                    }
                }
            )
        },
        onBackArrowClick = {
            onNavigate(Screen.InventoryScan(Screen.Inventory.routeId))
        }) { paddingValues ->
        SearchTagsScreenContent(
            modifier = Modifier.padding(paddingValues),
            rfidTagList = rfidTagList.value,
            generalState = generalState,
            appViewModel = appViewModel,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchTagsScreenContent(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    generalState: GeneralState,
    rfidTagList: List<InventoryItemMasterModel>,
) {

    val tagsToDisplay = rfidTagList.filter { it.epc in generalState.selectedTags.map { it.epc } }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    paleSkyBlue
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 20.sp,
                    text = stringResource(R.string.no_items_found)
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 34.sp,
                                color = deepOceanBlue,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(generalState.foundTags.size.toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 25.sp,
                                color = deepOceanBlue,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("/${tagsToDisplay.size}")
                        }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        LazyColumn {
            item {
                tagsToDisplay.forEach { item ->
                    SingleRfidRow(
                        boxWidth = TagDistanceCalculate.calculateBoxWidth(item.newField.rssi),
                        rfidNo = item.epc,
                        isPressed = item in generalState.foundTags,
                        onChange = {
                            appViewModel.onGeneralIntent(ShareIntent.ToggleFoundTag(item))
                        }
                    )
                }
            }
        }
    }
}