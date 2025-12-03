package com.example.sol_denka_stockmanagement.screen.outbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
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
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.ScanResultTable
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OutboundScreen(
    appViewModel: AppViewModel,
    outboundViewModel: OutboundViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState = appViewModel.inputState.collectAsStateWithLifecycle().value
    val checkedMap by appViewModel.perTagChecked.collectAsStateWithLifecycle()
    val processTypeMap by appViewModel.perTagHandlingMethod.collectAsStateWithLifecycle()
    val selectedCount by appViewModel.selectedCount.collectAsStateWithLifecycle()
    val outboundList by outboundViewModel.outboundList.collectAsStateWithLifecycle()

    LaunchedEffect(checkedMap, processTypeMap) {
        outboundViewModel.loadOutboundItems(checkedMap, processTypeMap)
    }

    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Outbound.routeId,
        prevScreenNameId = Screen.Outbound.routeId, // for scan screen to navigate back,
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
        Column(
            Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.planned_register_item_number,
                    selectedCount
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn(
                modifier = Modifier
                    .imePadding()
            ) {
                item {
                    ScanResultTable(
                        tableHeight = 250.dp,
                        tableHeader = listOf(
                            stringResource(R.string.item_name_title),
                            stringResource(R.string.item_code_title),
                            stringResource(R.string.handling_method)
                        ),
                        scanResult = outboundList.map { tag ->
                            ScanResultRowModel(
                                itemName = tag.itemName ?: "-",
                                itemCode = tag.epc,
                                lastColumn = tag.processType ?: "-"
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    InputFieldContainer(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = inputState.remark,
                        label = stringResource(R.string.occurred_at),
                        hintText = stringResource(R.string.occurred_at_hint),
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
                    Spacer(modifier = Modifier.height(10.dp))
                    InputFieldContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        value = inputState.remark,
                        label = "${stringResource(R.string.remark)} (オプション)",
                        hintText = stringResource(R.string.remark_hint),
                        isNumeric = false,
                        shape = RoundedCornerShape(13.dp),
                        readOnly = false,
                        isDropDown = false,
                        enable = true,
                        singleLine = false,
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
}