package com.example.sol_denka_stockmanagement.screen.inventory.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.Tab
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.OptionTabs
import com.example.sol_denka_stockmanagement.share.ScannedTagDisplay
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.share.dialog.RadioPowerDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.ui.theme.tealGreen
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScanScreen(
    prevScreenNameId: String,
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {
    val generalState = appViewModel.generalState.collectAsStateWithLifecycle().value
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle().value
    val readerSettingState by readerSettingViewModel.readerSettingState.collectAsStateWithLifecycle()
    var showRadioPowerDialog by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    val isPerformingInventory by scanViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, Screen.InventoryScan(""))
        appViewModel.apply {
            onGeneralIntent(
                ShareIntent.ToggleSelectionMode(false),
            )
            onGeneralIntent(
                ShareIntent.ClearTagSelectionList
            )
        }
    }

    RadioPowerDialog(
        showDialog = showRadioPowerDialog,
        readerSettingState = readerSettingState,
        onChangeMinPower = {
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        0
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(0))
            }
        },
        onChangeMaxPower = {
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        30
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(30))
            }
        },
        onChangeSlider = { newValue ->
            readerSettingViewModel.apply {
                onIntent(
                    ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                        newValue.toInt()
                    )
                )
                onIntent(ReaderSettingIntent.ChangeRadioPower(newValue.toInt()))
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
    ConfirmDialog(
        showDialog = showClearConfirmDialog,
        dialogTitle = stringResource(R.string.clear_processed_tag_dialog),
        buttonText1 = stringResource(R.string.ok),
        buttonText2 = stringResource(R.string.no),
        onOk = {
            scanViewModel.clearProcessedTag()
            appViewModel.onGeneralIntent(ShareIntent.ChangeTab(Tab.Left))
            showClearConfirmDialog = false
        },
        onCancel = { showClearConfirmDialog = false }
    )

    Layout(
        topBarText = Screen.Inventory.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.InventoryScan("").routeId,
        prevScreenNameId = prevScreenNameId,
        hasBottomBar = true,
        appViewModel = appViewModel,
        scanViewModel = scanViewModel,
        readerSettingViewModel = readerSettingViewModel,
        topBarButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                ButtonContainer(
                    buttonHeight = 35.dp,
                    buttonText = stringResource(R.string.finish_inventory),
                    buttonTextSize = 19,
                    canClick = isPerformingInventory.not() && rfidTagList.count { it.newField.tagStatus == TagStatus.PROCESSED } > 0,
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
                                enabled = isPerformingInventory.not(),
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
                if (generalState.isSelectionMode) {
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
                        containerColor = brightGreenSecondary,
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
                            shape = CircleShape
                        ),
                        shape = IconButtonDefaults.outlinedShape,
                        onClick = {
                            appViewModel.apply {
                                onGeneralIntent(
                                    ShareIntent.ToggleSelectionMode(false),
                                )
                                onGeneralIntent(
                                    ShareIntent.ClearTagSelectionList
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
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .shadow(
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
                        containerColor = if (isPerformingInventory) orange else tealGreen,
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
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
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
                    AnimatedContent(
                        targetState = generalState.tab,
                        transitionSpec = {
                            if (targetState == Tab.Left) {
                                slideInHorizontally { -it } + fadeIn() togetherWith
                                        slideOutHorizontally { it } + fadeOut()
                            } else {
                                slideInHorizontally { it } + fadeIn() togetherWith
                                        slideOutHorizontally { -it } + fadeOut()
                            }
                        },
                        label = "InventoryTabAnimation"
                    ) { tab ->

                        val displayList = when (tab) {
                            Tab.Left -> rfidTagList.filter { it.newField.tagStatus == TagStatus.UNPROCESSED }
                            Tab.Right -> rfidTagList.filter { it.newField.tagStatus == TagStatus.PROCESSED }
                        }

                        ScannedTagDisplay(
                            rfidTagList = displayList,
                            selectedTags = generalState.selectedTags,
                            isSelectionMode = if (generalState.selectedTags.isNotEmpty()) generalState.isSelectionMode else false,
                            onClick = { item ->
                                if (generalState.isSelectionMode) {
                                    appViewModel.onGeneralIntent(ShareIntent.ToggleTagSelection(item))
                                }
                            },
                            onLongClick = { item ->
                                appViewModel.apply {
                                    onGeneralIntent(ShareIntent.ToggleSelectionMode(true))
                                    onGeneralIntent(ShareIntent.ToggleTagSelection(item))
                                }
                            },
                            onCheckedChange = { item ->
                                appViewModel.onGeneralIntent(ShareIntent.ToggleTagSelection(item))
                            }
                        )
                    }
                }
            }
        }
    }
}