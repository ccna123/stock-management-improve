package com.example.sol_denka_stockmanagement.screen.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
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
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.scan.components.ShippingSingleItem
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.orange
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
    val scannedTag3 by scanViewModel.scannedTag3.collectAsStateWithLifecycle()
    val isPerformingInventory by scanViewModel.isPerformingInventory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val generalState = appViewModel.generalState.value
    val expandState = appViewModel.expandState.value
    val inputState = appViewModel.inputState.value
    val expandedMap by appViewModel.perTagExpanded.collectAsStateWithLifecycle()
    val handlingMap by appViewModel.perTagHandlingMethod.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, screen = Screen.Scan(""))
        appViewModel.onGeneralIntent(ShareIntent.ClearTagSelectionList)
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
                    containerColor = if (isPerformingInventory) orange else brightAzure,
                    onClick = {
                        scope.launch {
                            if (isPerformingInventory) scanViewModel.stopInventory() else scanViewModel.startInventory()
                        }
                    }
                )
                ButtonContainer(
                    buttonText = when (prevScreenNameId) {
                        Screen.Shipping.routeId -> stringResource(R.string.register_info)
                        Screen.StorageAreaChange.routeId -> stringResource(R.string.storage_area_change)
                        else -> ""
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.shadow(
                        elevation = 13.dp,
                        clip = true,
                        ambientColor = Color.Gray.copy(alpha = 0.5f),
                        spotColor = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    canClick = scannedTag3.isNotEmpty() && generalState.selectedTags1.isNotEmpty(),
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
                Text(text = stringResource(R.string.scan_tag_list_title))
                ButtonContainer(
                    buttonText = if (generalState.isAllSelected) stringResource(R.string.select_all_remove) else stringResource(
                        R.string.select_all
                    ),
                    containerColor = if (generalState.isAllSelected) Color.Red else brightAzure,
                    canClick = scannedTag3.isNotEmpty(),
                    onClick = {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ToggleSelectionAll(
                                tagList = scannedTag3
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(scannedTag3.toList()) { tag ->
                    val isExpanded = expandedMap[tag] ?: false
                    val value = handlingMap[tag] ?: ""
                    ShippingSingleItem(
                        tag = tag,
                        isChecked = tag in generalState.selectedTags1,
                        onSelect = {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ToggleTagSelection1(
                                    tag = tag,
                                    totalTag = scannedTag3.size
                                )
                            )
                        },
                        onCheckedChange = {
                            appViewModel.onGeneralIntent(
                                ShareIntent.ToggleTagSelection1(
                                    tag = tag,
                                    totalTag = scannedTag3.size
                                )
                            )
                        },
                        isExpanded = isExpanded,
                        value = value,
                        onExpandedChange = {
                            appViewModel.onExpandIntent(ExpandIntent.TogglePerTagHandlingExpanded(tag))
                        },
                        onDismissRequest = {
                            appViewModel.onExpandIntent(
                                ExpandIntent.CloseHandlingExpanded(tag)
                            )
                        },
                        onValueChange = { newValue ->
                            appViewModel.onGeneralIntent(
                                ShareIntent.ChangePerTagHandlingMethod(tag, newValue)
                            )
                        },
                        onClickInput = { appViewModel.onExpandIntent(ExpandIntent.CloseHandlingExpanded(tag)) },
                        onClickDropDownMenuItem = { method ->
                            appViewModel.onGeneralIntent(
                                ShareIntent.ChangePerTagHandlingMethod(tag, method)
                            )
                            appViewModel.onExpandIntent(
                                ExpandIntent.CloseHandlingExpanded(tag)
                            )
                        }
                    )
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable(
//                                onClick = {
//                                    appViewModel.onGeneralIntent(
//                                        ShareIntent.ToggleTagSelection1(
//                                            tag = tag,
//                                            totalTag = scannedTag3.size
//                                        )
//                                    )
//                                }
//                            ),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(5.dp)
//                        ) {
//                            Text(text = MaterialSelectionItem.MISS_ROLL.displayName)
//                            Text(text = tag)
//                        }
//                        Checkbox(
//                            checked = tag in generalState.selectedTags1,
//                            onCheckedChange = {
//                                appViewModel.onGeneralIntent(
//                                    ShareIntent.ToggleTagSelection1(
//                                        tag = tag,
//                                        totalTag = scannedTag3.size
//                                    )
//                                )
//                            }
//                        )
//                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}