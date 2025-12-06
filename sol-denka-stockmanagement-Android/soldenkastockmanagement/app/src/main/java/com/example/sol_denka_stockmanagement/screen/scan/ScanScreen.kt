package com.example.sol_denka_stockmanagement.screen.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.scan.components.InboundScanTagCard
import com.example.sol_denka_stockmanagement.screen.scan.components.LocationChangeSingleItem
import com.example.sol_denka_stockmanagement.screen.scan.components.OutboundSingleItem
import com.example.sol_denka_stockmanagement.screen.scan.components.ProcessModal
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.ui.theme.tealGreen
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ScanScreen(
    scanViewModel: ScanViewModel,
    appViewModel: AppViewModel,
    prevScreenNameId: String,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {
    val scannedTags by scanViewModel.scannedTags.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val isPerformingInventory by appViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val expandedMap by appViewModel.perTagExpanded.collectAsStateWithLifecycle()
    val processMap by appViewModel.perTagProcessMethod.collectAsStateWithLifecycle()
    val checkedMap by appViewModel.perTagChecked.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()

    val isAllSelected by appViewModel.isAllSelected.collectAsStateWithLifecycle()
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()
    val showModalProcessMethod by appViewModel.showModalProcessMethod.collectAsStateWithLifecycle()
    val showClearTagConfirmDialog = appViewModel.showClearTagConfirmDialog.value

    LaunchedEffect(Unit) {
        val mode = when (prevScreenNameId) {
            Screen.Inbound.routeId -> ScanMode.INBOUND
            Screen.Outbound.routeId -> ScanMode.OUTBOUND
            Screen.LocationChange.routeId -> ScanMode.LOCATION_CHANGE
            else -> ScanMode.NONE
        }
        scanViewModel.setScanMode(mode)
        scanViewModel.setEnableScan(true)
    }

    LaunchedEffect(scannedTags) {
        val checkedMap = appViewModel.perTagChecked.value
        val selectedCount = checkedMap.count { (key, checked) ->
            scannedTags.keys.contains(key) && checked
        }

        val allSelected = scannedTags.isNotEmpty() && scannedTags.keys.all { key ->
            checkedMap[key] == true
        }
        appViewModel.onGeneralIntent(
            ShareIntent.UpdateSelectionStatus(
                selectedCount = selectedCount,
                allSelected = allSelected
            )
        )
    }


    if (showModalProcessMethod) {
        ProcessModal(
            selectedCount = selectedCount,
            chosenMethod = inputState.processMethod,
            onChooseMethod = { method ->
                appViewModel.onInputIntent(InputIntent.ChangeProcessMethod(method))
            },
            onDismissRequest = {
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowModalProcessMethod(
                        false
                    )
                )
            },
            onApplyBulk = {
                appViewModel.apply {
                    onInputIntent(InputIntent.BulkApplyProcessMethod)
                    onGeneralIntent(ShareIntent.ShowModalProcessMethod(false))
                }
            }
        )
    }

    ConfirmDialog(
        showDialog = showClearTagConfirmDialog,
        dialogTitle = stringResource(R.string.clear_processed_tag_dialog),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    onClick = {
                        scanViewModel.clearInboundDetail()
                        scanViewModel.clearTagStatusAndRssi()
                        scanViewModel.setScanMode(ScanMode.NONE)
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
        topBarText = Screen.fromRouteId(prevScreenNameId)?.displayName ?: "",
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.Scan("").routeId,
        scanViewModel = scanViewModel,
        hasBottomBar = true,
        appViewModel = appViewModel,
        bottomButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ButtonContainer(
                    buttonText = if (isPerformingInventory) stringResource(R.string.scan_stop) else stringResource(
                        R.string.scan_start
                    ),
                    containerColor = if (isPerformingInventory) orange else tealGreen,
                    modifier = Modifier
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        )
                        .weight(1f)
                        .fillMaxWidth(),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.scanner),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        scope.launch {
                            if (isPerformingInventory) scanViewModel.stopInventory() else scanViewModel.startInventory()
                        }
                    }
                )
                ButtonContainer(
                    buttonText = when (prevScreenNameId) {
                        in listOf(
                            Screen.Outbound.routeId,
                            Screen.LocationChange.routeId
                        ) -> "${stringResource(R.string.register_info)}(${selectedCount}件)"

                        Screen.Inbound.routeId -> stringResource(R.string.register_info)
                        else -> ""
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    canClick = when (prevScreenNameId) {
                        in listOf(
                            Screen.Outbound.routeId,
                            Screen.LocationChange.routeId
                        ) -> checkedMap.values.any { it }

                        Screen.Inbound.routeId -> lastInboundEpc?.isNotEmpty() == true
                        else -> true
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.register),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        onNavigate(
                            when (prevScreenNameId) {
                                Screen.Outbound.routeId -> Screen.Outbound
                                Screen.LocationChange.routeId -> Screen.LocationChange
                                Screen.Inbound.routeId -> Screen.Inbound
                                else -> Screen.Home
                            }
                        )
                    }
                )
            }
        },
        onBackArrowClick = {
            if (isPerformingInventory.not()) {
                if (scannedTags.isNotEmpty() || lastInboundEpc?.isNotEmpty() == true) {
                    appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
                } else {
                    scanViewModel.setScanMode(ScanMode.NONE)
                    onGoBack()
                }
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            if (prevScreenNameId == Screen.Inbound.routeId) {
                val tag = rfidTagList.find { it.epc == lastInboundEpc }
                InboundScanTagCard(
                    epc = tag?.epc ?: "-",
                    itemName = tag?.newFields?.itemName ?: "-",
                    itemCode = tag?.newFields?.itemCode ?: "-",
                    timeStamp = if (tag == null) "-" else LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            } else {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.scan_tag_list_item))
                    ButtonContainer(
                        modifier = Modifier.width(120.dp),
                        buttonText = if (isAllSelected) stringResource(R.string.select_all_remove) else stringResource(
                            R.string.select_all
                        ),
                        containerColor = if (isAllSelected) Color.Red else brightAzure,
                        canClick = scannedTags.isNotEmpty() && isPerformingInventory.not(),
                        onClick = {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ToggleSelectionAll(
                                    tagList = scannedTags.keys
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(color = brightAzure)
                Spacer(modifier = Modifier.height(10.dp))
                if (prevScreenNameId == Screen.Outbound.routeId) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ButtonContainer(
                            modifier = Modifier
                                .fillMaxWidth(),
                            containerColor = brightGreenSecondary,
                            buttonText = stringResource(
                                R.string.bulk_register,
                                selectedCount.toString()
                            ),
                            canClick = checkedMap.values.any { it },
                            onClick = {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ShowModalProcessMethod(
                                        true
                                    )
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn {
                    items(scannedTags.toList(), key = { tag -> tag }) { tag ->
                        when (prevScreenNameId) {
                            Screen.Outbound.routeId -> {
                                val isExpanded = expandedMap[tag.first] ?: false
                                val isChecked = checkedMap[tag.first] ?: false
                                val value = processMap[tag.first] ?: ""
                                OutboundSingleItem(
                                    tag = rfidTagList.find { it.epc == tag.first }?.epc ?: "",
                                    itemName = rfidTagList.find { it.epc == tag.first }?.newFields?.itemName ?: "",
                                    isChecked = isChecked,
                                    onSelect = {
                                        if (isPerformingInventory.not()) {
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleTagSelection1(
                                                    tag = tag.first,
                                                    totalTag = scannedTags.size
                                                )
                                            )
                                        }
                                    },
                                    onCheckedChange = {
                                        if (isPerformingInventory.not()) {
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleTagSelection1(
                                                    tag = tag.first,
                                                    totalTag = scannedTags.size
                                                )
                                            )
                                        }
                                    },
                                    isExpanded = isExpanded,
                                    value = value,
                                    onExpandedChange = {
                                        if (isPerformingInventory.not()) {
                                            appViewModel.onExpandIntent(
                                                ExpandIntent.TogglePerTagProcessExpanded(
                                                    tag.first
                                                )
                                            )
                                        }
                                    },
                                    onDismissRequest = {
                                        appViewModel.onExpandIntent(
                                            ExpandIntent.CloseProcessExpanded(tag.first)
                                        )
                                    },
                                    onValueChange = { newValue ->
                                        appViewModel.onGeneralIntent(
                                            ShareIntent.ChangePerTagProcessMethod(
                                                tag = tag.first,
                                                method = newValue
                                            )
                                        )
                                    },
                                    onClickDropDownMenuItem = { method ->
                                        val finalValue =
                                            if (method == SelectTitle.SelectProcessMethod.displayName) ""
                                            else method

                                        appViewModel.onGeneralIntent(
                                            ShareIntent.ChangePerTagProcessMethod(
                                                tag.first,
                                                finalValue
                                            )
                                        )

                                        appViewModel.onExpandIntent(
                                            ExpandIntent.CloseProcessExpanded(tag.first)
                                        )
                                    }
                                )
                            }

                            Screen.LocationChange.routeId -> {
                                val isChecked = checkedMap[tag.first] ?: false
                                LocationChangeSingleItem(
                                    tag = rfidTagList.find { it.epc == tag.first }?.epc ?: "",
                                    itemName = rfidTagList.find { it.epc == tag.first }?.newFields?.itemName ?: "",
                                    isChecked = isChecked,
                                    onCheckedChange = {
                                        if (isPerformingInventory.not()){
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleTagSelection1(
                                                    tag = tag.first,
                                                    totalTag = scannedTags.size
                                                )
                                            )
                                        }
                                    },
                                    onClick = {
                                        if (isPerformingInventory.not()){
                                            appViewModel.onGeneralIntent(
                                                ShareIntent.ToggleTagSelection1(
                                                    tag = tag.first,
                                                    totalTag = scannedTags.size
                                                )
                                            )
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
}