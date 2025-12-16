package com.example.sol_denka_stockmanagement.screen.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.ScanMode
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
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
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
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
    val scope = rememberCoroutineScope()

    val scannedTags by scanViewModel.scannedTags.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()

    val isPerformingInventory by appViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val expandedMap by appViewModel.perTagExpanded.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val processTypeMaster by appViewModel.processTypeMaster.collectAsStateWithLifecycle()
    val outboundProcessErrorSet by appViewModel.outboundProcessErrorSet.collectAsStateWithLifecycle()
    val processMap by appViewModel.perTagProcessMethod.collectAsStateWithLifecycle()
    val showModalProcessMethod by appViewModel.showModalProcessMethod.collectAsStateWithLifecycle()
    val showClearTagConfirmDialog = appViewModel.showClearTagConfirmDialog.value

    val displayTags = rfidTagList.filter { it.epc in scannedTags }


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

    ProcessModal(
        showModalProcessMethod = showModalProcessMethod,
        processTypeList = processTypeMaster,
        selectedCount = rfidTagList.count { it.newFields.isChecked },
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
                onInputIntent(InputIntent.BulkApplyProcessMethod(checkedTags = displayTags.map { it.epc }))
                onGeneralIntent(ShareIntent.ShowModalProcessMethod(false))
            }
        }
    )

    ConfirmDialog(
        showDialog = showClearTagConfirmDialog,
        dialogTitle = stringResource(R.string.clear_processed_tag_dialog),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.yes),
                    onClick = {
                        scanViewModel.apply {
                            clearInboundDetail()
                            clearTagStatusAndRssi()
                            setScanMode(ScanMode.NONE)
                        }
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
                        ) -> "${stringResource(R.string.register_info)}(${rfidTagList.count { it.newFields.isChecked }}件)"

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
                        ) -> rfidTagList.any { it.newFields.isChecked }

                        Screen.Inbound.routeId -> isPerformingInventory.not() && lastInboundEpc?.isNotEmpty() == true
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
                        when (prevScreenNameId) {
                            Screen.Outbound.routeId -> {
                                val checkedTags = rfidTagList.filter { it.newFields.isChecked }
                                val missingProcess = checkedTags.filter { tag ->
                                    val method = processMap[tag.epc]
                                    method.isNullOrEmpty()
                                }
                                if (missingProcess.isNotEmpty()) {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.MarkOutboundProcessError(
                                            missingProcess.map { it.epc }
                                        )
                                    )
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ShowDialog(
                                            type = DialogType.ERROR,
                                            message = MessageMapper.toMessage(StatusCode.PROCESS_NOT_CHOSEN)
                                        )
                                    )
                                    return@ButtonContainer
                                }
                                appViewModel.onGeneralIntent(ShareIntent.ClearOutboundProcessError)
                                onNavigate(Screen.Outbound)
                            }

                            Screen.LocationChange.routeId -> onNavigate(Screen.LocationChange)
                            Screen.Inbound.routeId -> onNavigate(Screen.Inbound)
                            else -> onNavigate(Screen.Home)
                        }

                    }
                )
            }
        },
        onBackArrowClick = {
            if (isPerformingInventory.not()) {
                if (scannedTags.isNotEmpty() || lastInboundEpc?.isNotEmpty() == true) {
                    appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
                } else {
                    scanViewModel.apply {
                        setScanMode(ScanMode.NONE)
                        resetIsCheckedField()
                    }
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
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(12.dp))
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.scan_tag_list_item))
                            ButtonContainer(
                                modifier = Modifier.width(120.dp),
                                buttonText = if (displayTags.isNotEmpty() && displayTags.all { it.newFields.isChecked }) stringResource(
                                    R.string.select_all_remove
                                ) else stringResource(
                                    R.string.select_all
                                ),
                                shape = RoundedCornerShape(10.dp),
                                containerColor = if (displayTags.all { it.newFields.isChecked }) Color.Red else brightAzure,
                                canClick = scannedTags.isNotEmpty() && isPerformingInventory.not(),
                                onClick = {
                                    scanViewModel.toggleCheckAll(
                                        targetEpcs = scannedTags.keys,
                                        value = !displayTags.all { it.newFields.isChecked })
                                }
                            )
                        }
                        if (prevScreenNameId == Screen.Outbound.routeId) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                ButtonContainer(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    containerColor = brightGreenSecondary,
                                    buttonText = stringResource(
                                        R.string.bulk_register,
                                        rfidTagList.count { it.newFields.isChecked }.toString()
                                    ),
                                    canClick = rfidTagList.any { it.newFields.isChecked },
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
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                LazyColumn {
                    items(scannedTags.toList(), key = { tag -> tag }) { tag ->
                        when (prevScreenNameId) {
                            Screen.Outbound.routeId -> {
                                val isExpanded = expandedMap[tag.first] ?: false
                                val value = processMap[tag.first] ?: ""
                                OutboundSingleItem(
                                    processTypeList = processTypeMaster,
                                    tag = rfidTagList.find { it.epc == tag.first }?.epc ?: "",
                                    itemName = rfidTagList.find { it.epc == tag.first }?.newFields?.itemName
                                        ?: "",
                                    isChecked = rfidTagList.find { it.epc == tag.first }?.newFields?.isChecked
                                        ?: false,
                                    isError = outboundProcessErrorSet.contains(tag.first),
                                    onSelect = {
                                        if (isPerformingInventory.not()) {
                                            scanViewModel.toggleCheck(tag.first)
                                        }
                                    },
                                    onCheckedChange = {
                                        if (isPerformingInventory.not()) {
                                            scanViewModel.toggleCheck(tag.first)
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
                                        appViewModel.onGeneralIntent(
                                            ShareIntent.ChangePerTagProcessMethod(
                                                tag = tag.first,
                                                method = method
                                            )
                                        )

                                        appViewModel.onExpandIntent(
                                            ExpandIntent.CloseProcessExpanded(tag.first)
                                        )
                                    }
                                )
                            }

                            Screen.LocationChange.routeId -> {
                                LocationChangeSingleItem(
                                    tag = rfidTagList.find { it.epc == tag.first }?.epc ?: "",
                                    itemName = rfidTagList.find { it.epc == tag.first }?.newFields?.itemName
                                        ?: "",
                                    isChecked = rfidTagList.find { it.epc == tag.first }?.newFields?.isChecked
                                        ?: false,
                                    onCheckedChange = {
                                        if (isPerformingInventory.not()) {
                                            scanViewModel.toggleCheck(tag.first)
                                        }
                                    },
                                    onClick = {
                                        if (isPerformingInventory.not()) {
                                            scanViewModel.toggleCheck(tag.first)
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