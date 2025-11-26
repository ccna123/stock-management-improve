package com.example.sol_denka_stockmanagement.screen.shipping

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.ScanResultRowModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.ScanResultTable
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShippingScreen(
    appViewModel: AppViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val errorState = appViewModel.errorState.value
    val expandState = appViewModel.expandState.collectAsStateWithLifecycle().value
    val inputState = appViewModel.inputState.collectAsStateWithLifecycle().value
    val generalState = appViewModel.generalState.collectAsStateWithLifecycle().value
    val checkedMap by appViewModel.perTagHandlingMethod.collectAsStateWithLifecycle()
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()

    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Shipping.routeId,
        prevScreenNameId = Screen.Shipping.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    appViewModel.onGeneralIntent(
                        ShareIntent.SaveScanResult(
                            data = listOf()
                        )
                    )
                },
                buttonText = stringResource(R.string.register),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(16.dp)
                .imePadding()
        ) {
            item {
                Text(text = stringResource(R.string.planned_register_item_number, selectedCount))
                Spacer(modifier = Modifier.height(18.dp))
                ScanResultTable(
                    tableHeight = 250.dp,
                    tableHeader = listOf(
                        stringResource(R.string.item_name_title),
                        stringResource(R.string.item_code_title),
                        stringResource(R.string.handling_method)
                    ),
                    scanResult = checkedMap.keys.map { tag ->
                        ScanResultRowModel(
                            itemName = MaterialSelectionItem.MISS_ROLL.displayName,
                            itemCode = tag,
                            lastColumn = checkedMap[tag] ?: "未設定"
                        )
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                InputFieldContainer(
                    modifier = Modifier.fillMaxWidth(),
                    value = inputState.remark,
                    label = "備考",
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