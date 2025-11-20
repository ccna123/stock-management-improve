package com.example.sol_denka_stockmanagement.screen.scan

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.sol_denka_stockmanagement.constant.HandlingMethod
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.scan.components.ShippingModal
import com.example.sol_denka_stockmanagement.screen.scan.components.ShippingSingleItem
import com.example.sol_denka_stockmanagement.screen.scan.components.StorageAreaChangeSingleItem
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.deepOceanBlue
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.ui.theme.tealGreen
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ScanScreen(
    scanViewModel: ScanViewModel,
    appViewModel: AppViewModel,
    prevScreenNameId: String,
    onNavigate: (Screen) -> Unit
) {
    val scannedTags3 by scanViewModel.scannedTags3.collectAsStateWithLifecycle()
    val isPerformingInventory by scanViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val expandedMap by appViewModel.perTagExpanded.collectAsStateWithLifecycle()
    val handlingMap by appViewModel.perTagHandlingMethod.collectAsStateWithLifecycle()
    val checkedMap by appViewModel.perTagChecked.collectAsStateWithLifecycle()

    val isAllSelected by appViewModel.isAllSelected.collectAsStateWithLifecycle()
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()
    val showBottomSheet by appViewModel.showBottomSheet.collectAsStateWithLifecycle()


    if (showBottomSheet) {
        ShippingModal(
            selectedCount = selectedCount,
            chosenMethod = appViewModel.bottomSheetChosenMethod.value,
            onChooseMethod = { method ->
                appViewModel.onInputIntent(InputIntent.ChangeHandlingMethod(method))
            },
            onDismissRequest = { appViewModel.onGeneralIntent(ShareIntent.ShowBottomSheet(false)) },
            onApplyBulk = {
                appViewModel.apply {
                    onInputIntent(InputIntent.BulkApplyHandlingMethod)
                    onGeneralIntent(ShareIntent.ShowBottomSheet(false))
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, screen = Screen.Scan(""))
//        appViewModel.onGeneralIntent(ShareIntent.ClearTagSelectionList)
    }

    Layout(
        topBarText = Screen.fromRouteId(prevScreenNameId)?.displayName ?: "",
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.Scan("").routeId,
        onNavigate = onNavigate,
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
                    shape = RoundedCornerShape(10.dp),
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
                    buttonText = "${stringResource(R.string.register_info)}(${selectedCount}件)",
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    canClick = checkedMap.values.any { it },
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
                                Screen.Shipping.routeId -> Screen.Shipping
                                Screen.StorageAreaChange.routeId -> Screen.StorageAreaChange
                                else -> Screen.Home
                            }
                        )
                    }
                )
            }
        },
        onBackArrowClick = {
            onNavigate(Screen.Home)
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
        ) {
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
                    canClick = scannedTags3.isNotEmpty(),
                    onClick = {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ToggleSelectionAll(
                                tagList = scannedTags3.toSet()
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider(color = brightAzure)
            Spacer(modifier = Modifier.height(10.dp))
            if (prevScreenNameId == Screen.Shipping.routeId) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ButtonContainer(
                        modifier = Modifier
                            .fillMaxWidth(),
                        buttonText = stringResource(
                            R.string.bulk_register,
                            selectedCount.toString()
                        ),
                        canClick = checkedMap.values.any { it },
                        onClick = {
                            appViewModel.onGeneralIntent(ShareIntent.ShowBottomSheet(true))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(scannedTags3.toList(), key = { tag -> tag }) { tag ->
                    when (prevScreenNameId) {
                        Screen.Shipping.routeId -> {
                            val isExpanded = expandedMap[tag] ?: false
                            val isChecked = checkedMap[tag] ?: false
                            val value = handlingMap[tag] ?: ""
                            ShippingSingleItem(
                                tag = tag,
                                isChecked = isChecked,
                                onSelect = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleTagSelection1(
                                            tag = tag,
                                            totalTag = scannedTags3.size
                                        )
                                    )
                                },
                                onCheckedChange = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleTagSelection1(
                                            tag = tag,
                                            totalTag = scannedTags3.size
                                        )
                                    )
                                },
                                isExpanded = isExpanded,
                                value = value,
                                onExpandedChange = {
                                    appViewModel.onExpandIntent(
                                        ExpandIntent.TogglePerTagHandlingExpanded(
                                            tag
                                        )
                                    )
                                },
                                onDismissRequest = {
                                    appViewModel.onExpandIntent(
                                        ExpandIntent.CloseHandlingExpanded(tag)
                                    )
                                },
                                onValueChange = { newValue ->
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ChangePerTagHandlingMethod(
                                            tag = tag,
                                            method = newValue
                                        )
                                    )
                                },
//                                onClickInput = {
//                                    appViewModel.onExpandIntent(
//                                        ExpandIntent.CloseHandlingExpanded(
//                                            tag
//                                        )
//                                    )
//                                },
                                onClickDropDownMenuItem = { method ->
                                    val finalValue =
                                        if (method == HandlingMethod.SELECTION_TITLE.displayName) ""
                                        else method

                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ChangePerTagHandlingMethod(tag, finalValue)
                                    )

                                    appViewModel.onExpandIntent(
                                        ExpandIntent.CloseHandlingExpanded(tag)
                                    )
                                }
                            )
                        }

                        Screen.StorageAreaChange.routeId -> {
                            val isChecked = checkedMap[tag] ?: false
                            StorageAreaChangeSingleItem(
                                tag = tag,
                                isChecked = isChecked,
                                onCheckedChange = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleTagSelection1(
                                            tag = tag,
                                            totalTag = scannedTags3.size
                                        )
                                    )
                                },
                                onClick = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleTagSelection1(
                                            tag = tag,
                                            totalTag = scannedTags3.size
                                        )
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = brightAzure)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}