package com.example.sol_denka_stockmanagement.screen.storage_area_change

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StockAreaItem
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.TableCell
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StorageAreaChangeScreen(
    appViewModel: AppViewModel,
    onNavigate: (Screen) -> Unit
) {

    val errorState = appViewModel.errorState.value
    val expandState = appViewModel.expandState.value
    val inputState = appViewModel.inputState.value
    val generalState = appViewModel.generalState.value
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()
    val checkedMap by appViewModel.perTagChecked.collectAsStateWithLifecycle()

    Layout(
        topBarText = stringResource(R.string.storage_area_change),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Shipping.routeId,
        prevScreenNameId = Screen.Shipping.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .shadow(
                        elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                        spotColor = Color.DarkGray.copy(alpha = 0.7f)
                    ),
                shape = RoundedCornerShape(10.dp),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                },
                buttonText = stringResource(R.string.storage_area_change),
            )
        },
        onBackArrowClick = {
            onNavigate(Screen.Scan(Screen.StorageAreaChange.routeId))
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .imePadding()
        ) {
            item {
                Text(text = stringResource(R.string.planned_register_item_number, selectedCount))
                Spacer(modifier = Modifier.height(18.dp))
                Column{
                    val localTempFontSize = compositionLocalOf { 13.sp }
                    CompositionLocalProvider(localTempFontSize provides localTempFontSize.current) {
                        Row(
                            modifier = Modifier
                                .background(color = paleSkyBlue)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TableCell(
                                content = stringResource(R.string.item_name_title),
                                contentSize = localTempFontSize.current,
                                weight = 1f
                            )
                            TableCell(
                                content = stringResource(R.string.item_code_title),
                                contentSize = localTempFontSize.current,
                                weight = 1f
                            )
                            TableCell(
                                content = stringResource(R.string.storage_area),
                                contentSize = localTempFontSize.current,
                                weight = 1f
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .height(150.dp)
                    ) {
                        items(checkedMap.filter { it.value }.keys.toList()) { tag ->
                            Row(
                                modifier = Modifier
                                    .height(IntrinsicSize.Min)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableCell(
                                    content = MaterialSelectionItem.MISS_ROLL.displayName,
                                    weight = 1f
                                )
                                TableCell(
                                    content = tag,
                                    weight = 1f
                                )
                                TableCell(
                                    content = "保管場所A",
                                    weight = 1f
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                ExposedDropdownMenuBox(
                    expanded = expandState.stockAreaExpanded,
                    onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleStockAreaExpanded) }) {
                    InputFieldContainer(
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth(),
                        value = if (inputState.stockArea == StockAreaItem.SELECTION_TITLE.displayName) "" else inputState.stockArea,
                        hintText = StockAreaItem.SELECTION_TITLE.displayName,
                        isNumeric = false,
                        shape = RoundedCornerShape(13.dp),
                        onChange = { newValue ->
                            appViewModel.onInputIntent(
                                InputIntent.ChangeStockArea(
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
                        expanded = expandState.stockAreaExpanded,
                        onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleStockAreaExpanded) }
                    ) {
                        listOf(
                            StockAreaItem.SELECTION_TITLE.displayName,
                            StockAreaItem.STOCK_AREA1.displayName,
                            StockAreaItem.STOCK_AREA2.displayName,
                            StockAreaItem.STOCK_AREA3.displayName,
                            StockAreaItem.STOCK_AREA4.displayName,
                            StockAreaItem.STOCK_AREA5.displayName,
                        ).forEach { stockArea ->
                            DropdownMenuItem(
                                text = { Text(text = stockArea) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(InputIntent.ChangeStockArea(if (stockArea == SelectTitle.SelectStockArea.displayName) "" else stockArea))
                                        onExpandIntent(ExpandIntent.ToggleStockAreaExpanded)
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                InputFieldContainer(
                    modifier = Modifier.fillMaxWidth(),
                    value = inputState.remark,
                    label = stringResource(R.string.remark) + "（オプション）",
                    hintText = stringResource(R.string.remark_hint),
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = false,
                    isDropDown = false,
                    enable = true,
                    onChange = { newValue ->
                        val filteredValue = newValue.trimStart().filter { char ->
                            (char.isLetterOrDigit() && char.toString()
                                .toByteArray().size == 1) || char == '-'
                        }
                        appViewModel.onInputIntent(
                            InputIntent.ChangeRemark(
                                filteredValue
                            )
                        )
                    }
                )
            }
        }
    }
}