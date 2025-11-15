package com.example.sol_denka_stockmanagement.screen.inventory.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.intent.InventoryScanIntent
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.OptionTabs
import com.example.sol_denka_stockmanagement.share.ScannedTagDisplay
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.share.AppDialog
import com.example.sol_denka_stockmanagement.share.RadioPowerDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScanScreen(
    prevScreenNameId: String,
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    inventoryScanViewModel: InventoryScanViewModel,
    onNavigate: (Screen) -> Unit
) {
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val readerSettingState by readerSettingViewModel.readerSettingState.collectAsStateWithLifecycle()
    var showRadioPowerDialog by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    val isPerformingInventory by scanViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, Screen.InventoryScan(""))
        inventoryScanViewModel.apply {
            onIntent(
                InventoryScanIntent.ToggleSelectionMode(false),
            )
            onIntent(
                InventoryScanIntent.ClearTagSelectionList
            )
        }
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

    if (showClearConfirmDialog) {
        AppDialog {
            Text(
                text = stringResource(R.string.clear_processed_tag_dialog),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    shape = RoundedCornerShape(10.dp),
                    buttonTextSize = 20,
                    onClick = {
                        scanViewModel.clearProcessedTag()
                        appViewModel.onGeneralIntent(ShareIntent.ChangeTab(Tab.Left))
                        showClearConfirmDialog = false
                    },
                )
                ButtonContainer(
                    buttonText = stringResource(R.string.close),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = Color.Red,
                    buttonTextSize = 20,
                    onClick = {
                        showClearConfirmDialog = false
                    },
                )
            }
        }
    }

    Layout(
        topBarText = Screen.Inventory.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.InventoryScan("").routeId,
        prevScreenNameId = prevScreenNameId,
        onNavigate = onNavigate,
        hasBottomBar = true,
        appViewModel = appViewModel,
        readerSettingViewModel = readerSettingViewModel,
        topBarButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                ButtonContainer(
                    buttonHeight = 35.dp,
                    shape = RoundedCornerShape(12.dp),
                    buttonText = stringResource(R.string.finish_inventory),
                    buttonTextSize = 19,
                    canClick = rfidTagList.value.count { it.newField.tagStatus == TagStatus.PROCESSED } > 0,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    },
                    containerColor = orange,
                    onClick = {
                        onNavigate(Screen.InventoryComplete)
                    }
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
                                showClearConfirmDialog = true
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ToggleDropDown(false)
                                )
                            }
                        )
                    }
                }
            }
        },
        bottomButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (inventoryScanViewModel.isSelectionMode.value) {
                    ButtonContainer(
                        buttonText = stringResource(R.string.search),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        },
                        onClick = {
                            onNavigate(Screen.SearchTagsScreen(prevScreenNameId))
                        },
                    )
                    ButtonContainer(
                        containerColor = brightOrange,
                        buttonText = stringResource(R.string.detail),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        },
                        onClick = {
                            onNavigate(Screen.Detail)
                        },
                    )
                    IconButton(
                        modifier = Modifier.background(
                            color = Color.Red,
                            shape = RoundedCornerShape(12.dp)
                        ),
                        shape = IconButtonDefaults.outlinedShape,
                        onClick = {
                            inventoryScanViewModel.apply {
                                onIntent(
                                    InventoryScanIntent.ToggleSelectionMode(false),
                                )
                                onIntent(
                                    InventoryScanIntent.ClearTagSelectionList
                                )
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                } else {
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
                }

            }
        },
        onBackArrowClick = {
            onNavigate(Screen.Inventory)
        }) { paddingValues ->
        InventoryScanScreenContent(
            modifier = Modifier.padding(paddingValues),
            appViewModel = appViewModel,
            generalState = generalState,
            inventoryScanViewModel = inventoryScanViewModel,
            rfidTagList = rfidTagList.value,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScanScreenContent(
    modifier: Modifier,
    generalState: GeneralState,
    appViewModel: AppViewModel,
    rfidTagList: List<InventoryItemMasterModel>,
    inventoryScanViewModel: InventoryScanViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                        .background(
                            color = Color.Unspecified,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.process_status), fontSize = 26.sp)
                        Text(
                            text = "${rfidTagList.count { it.newField.tagStatus == TagStatus.PROCESSED }}/${rfidTagList.size}",
                            fontSize = 26.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    OptionTabs(
                        tab = generalState.tab,
                        leftTabText = stringResource(R.string.unprocessed),
                        rightTabText = stringResource(R.string.processed),
                        leftTab = Tab.Left,
                        rightTab = Tab.Right,
                        leftIcon = Icons.Default.PendingActions,
                        rightIcon = Icons.Default.CheckCircle,
                        onChangeTab = {
                            appViewModel.onGeneralIntent(ShareIntent.ChangeTab(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ScannedTagDisplay(
                        rfidTagList = when (generalState.tab) {
                            Tab.Left -> rfidTagList.filter { it.newField.tagStatus == TagStatus.UNPROCESSED }
                            Tab.Right -> rfidTagList.filter { it.newField.tagStatus == TagStatus.PROCESSED }
                        },
                        selectedTags = inventoryScanViewModel.selectedTags.value,
                        isSelectionMode = if (inventoryScanViewModel.selectedTags.value.isNotEmpty()) inventoryScanViewModel.isSelectionMode.value else false,
                        onClick = { item ->
                            if (inventoryScanViewModel.isSelectionMode.value) {
                                inventoryScanViewModel.onIntent(
                                    InventoryScanIntent.ToggleTagSelection(item)
                                )
                            }
                        },
                        onLongClick = { item ->
                            inventoryScanViewModel.apply {
                                onIntent(
                                    InventoryScanIntent.ToggleSelectionMode(true)
                                )
                                onIntent(InventoryScanIntent.ToggleTagSelection(item))
                            }
                        },
                        onCheckedChange = { item ->
                            inventoryScanViewModel.onIntent(
                                InventoryScanIntent.ToggleTagSelection(item)
                            )
                        }
                    )
                }
            }
        }
    }
}
