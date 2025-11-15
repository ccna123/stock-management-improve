package com.example.sol_denka_stockmanagement.screen.scan.shipping

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
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShippingScanScreen(
    scanViewModel: ScanViewModel,
    appViewModel: AppViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    shippingScanViewModel: ShippingScanViewModel,
    onNavigate: (Screen) -> Unit
) {
    val scannedTag3 by scanViewModel.scannedTag3.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(enabled = true, screen = Screen.ShippingScan)
        shippingScanViewModel.apply {
            handle(ShippingScanIntent.ClearTagSelectionList)
            handle(ShippingScanIntent.ResetState)
        }
    }

    Layout(
        topBarText = Screen.ShippingScan.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        currentScreenNameId = Screen.ShippingScan.routeId,
        onNavigate = onNavigate,
        hasBottomBar = true,
        appViewModel = appViewModel,
        readerSettingViewModel = readerSettingViewModel,
        bottomButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ButtonContainer(
                    buttonText = stringResource(R.string.register_info),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.shadow(
                        elevation = 13.dp,
                        clip = true,
                        ambientColor = Color.Gray.copy(alpha = 0.5f),
                        spotColor = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                    canClick = scannedTag3.isNotEmpty(),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.register),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        onNavigate(Screen.Shipping)
                    }
                )
            }
        },
        onBackArrowClick = {
            onNavigate(Screen.Home)
        }) { paddingValues ->
        ShippingScanScreenContent(
            modifier = Modifier.padding(paddingValues),
            scannedTag = scannedTag3,
            shippingScanViewModel = shippingScanViewModel
        )
    }
}

@Composable
fun ShippingScanScreenContent(
    modifier: Modifier = Modifier,
    scannedTag: Set<String>,
    shippingScanViewModel: ShippingScanViewModel
) {
    Column(
        modifier = modifier
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
                buttonText = if (shippingScanViewModel.isAllSelected.value) stringResource(R.string.select_all_remove) else stringResource(
                    R.string.select_all
                ),
                containerColor = if (shippingScanViewModel.isAllSelected.value) Color.Red else brightAzure,
                canClick = scannedTag.isNotEmpty(),
                onClick = {
                    shippingScanViewModel.handle(
                        ShippingScanIntent.ToggleSelectionAll(
                            tagList = scannedTag
                        )
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(scannedTag.toList()) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                shippingScanViewModel.handle(
                                    ShippingScanIntent.ToggleTagSelection(
                                        tag = tag,
                                        totalTag = scannedTag.size
                                    )
                                )
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(text = MaterialSelectionItem.MISS_ROLL.displayName)
                        Text(text = tag)
                    }
                    Checkbox(
                        checked = tag in shippingScanViewModel.selectedTags.value,
                        onCheckedChange = {
                            shippingScanViewModel.handle(
                                ShippingScanIntent.ToggleTagSelection(
                                    tag = tag,
                                    totalTag = scannedTag.size
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}