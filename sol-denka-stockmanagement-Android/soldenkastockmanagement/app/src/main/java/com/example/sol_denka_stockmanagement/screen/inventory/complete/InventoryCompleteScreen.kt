package com.example.sol_denka_stockmanagement.screen.inventory.complete

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.sol_denka_stockmanagement.constant.InventoryScanResult
import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.inventory.InventoryCompleteModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.dialog.NetworkDialog
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryCompleteScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inventoryCompleteViewModel: InventoryCompleteViewModel,
    onGoBack: () -> Unit,
) {

    val generalState = appViewModel.generalState.collectAsStateWithLifecycle().value
    val inputState = appViewModel.inputState.collectAsStateWithLifecycle().value
    val rfidTagList = scanViewModel.rfidTagList.collectAsStateWithLifecycle().value
    val wrongLocationCount =
        inventoryCompleteViewModel.wrongLocationCount.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    LaunchedEffect(rfidTagList) {
        inventoryCompleteViewModel.computeWrongLocation(
            rfidTagList = rfidTagList.filter { it.newFields.tagStatus == TagStatus.PROCESSED },
            locationName = inputState.location
        )
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(
                ShareIntent.ToggleNetworkDialog(false)
            )
        })
    }
    val inventoryStatusList = listOf(
        InventoryCompleteModel(
            status = InventoryScanResult.OK,
            icon = R.drawable.scan_ok,
            color = brightGreenPrimary,
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.SHORTAGE,
            icon = R.drawable.scan_shortage,
            color = brightOrange,
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.OVERLOAD,
            icon = R.drawable.scan_overload,
            color = Color(0xFFF44336),
        ),
        InventoryCompleteModel(
            status = InventoryScanResult.WRONG_LOCATION,
            icon = R.drawable.scan_wrong_location,
            color = deepBlueSky,
        )
    )

    Layout(
        topBarText = Screen.InventoryComplete.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.InventoryComplete.routeId,
        prevScreenNameId = Screen.InventoryComplete.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
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
                    if (appViewModel.isNetworkConnected.value.not()) {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ToggleNetworkDialog(true)
                        )
                    }
                },
                buttonText = stringResource(R.string.finish_inventory),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 20.dp, horizontal = 30.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "以下の内容でCSVファイルを出力します")
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false, // 👈 allow the shadow to bleed outside the box
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(10.dp)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    inventoryStatusList.forEach { item ->
                        val count = when (item.status) {
                            InventoryScanResult.OK -> 123
                            InventoryScanResult.SHORTAGE -> 123
                            InventoryScanResult.OVERLOAD -> 123
                            InventoryScanResult.WRONG_LOCATION -> wrongLocationCount
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = null,
                                    tint = item.color,
                                    modifier = Modifier.size(23.dp)
                                )
                                Text(text = item.status.displayName, fontSize = 18.sp)
                            }
                            Text(
                                text = "$count 件",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}