package com.example.sol_denka_stockmanagement.presentation.inventory.input

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.CardContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(android.os.Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inventoryViewModel: InventoryViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {
    val uiState by inventoryViewModel.uiState.collectAsStateWithLifecycle()
    val locationMaster by inventoryViewModel.locationMaster.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val showClearTagConfirmDialog = appViewModel.showClearTagConfirmDialog.value

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    ConfirmDialog(
        showDialog = showClearTagConfirmDialog,
        dialogTitle = stringResource(R.string.clear_processed_tag_dialog),
        buttons = listOf(
            {
                ButtonContainer(
                    buttonText = stringResource(R.string.ok),
                    onClick = {
                        scanViewModel.clearTagStatusAndRssi()
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
        topBarText = stringResource(R.string.inventory),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp,
                    clip = true,
                    ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.scan),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                canClick = uiState.location != null,
                onClick = {
                    onNavigate(Screen.InventoryScan(Screen.Inventory.routeId))
                },
                buttonText = stringResource(R.string.inventory_start),
            )
        },
        onBackArrowClick = {
            if (rfidTagList.count { it.newFields.tagScanStatus == TagScanStatus.PROCESSED } > 0) {
                appViewModel.onGeneralIntent(ShareIntent.ToggleClearTagConfirmDialog)
            } else {
                onGoBack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CardContainer {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "棚卸を行う保管場所を選択")
                    Spacer(modifier = Modifier.height(10.dp))
                    ExposedDropdownMenuBox(
                        expanded = uiState.locationExpanded,
                        onExpandedChange = {
                            inventoryViewModel.onIntent(InventoryIntent.ToggleLocationExpanded)
                        }
                    ) {
                        InputFieldContainer(
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            value = if (uiState.location?.locationName == SelectTitle.SelectLocation.displayName) ""
                            else uiState.location?.locationName ?: "",
                            hintText = SelectTitle.SelectLocation.displayName,
                            isNumeric = false,
                            onChange = {},
                            readOnly = true,
                            isDropDown = true,
                            enable = true,
                            onEnterPressed = {}
                        )
                        ExposedDropdownMenu(
                            expanded = uiState.locationExpanded,
                            onDismissRequest = {
                                inventoryViewModel.onIntent(InventoryIntent.ToggleLocationExpanded)
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = SelectTitle.SelectLocation.displayName) },
                                onClick = {
                                    inventoryViewModel.onIntent(InventoryIntent.LocationChanged(null))
                                }
                            )
                            locationMaster.forEach { location ->
                                DropdownMenuItem(
                                    text = { Text(location.locationName) },
                                    onClick = {
                                        inventoryViewModel.onIntent(
                                            InventoryIntent.LocationChanged(location)
                                        )
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