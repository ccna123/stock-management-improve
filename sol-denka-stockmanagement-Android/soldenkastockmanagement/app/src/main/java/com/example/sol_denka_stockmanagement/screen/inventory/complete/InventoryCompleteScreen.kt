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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.InventoryScanResult
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.model.InventoryScanResultStatusModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.share.NetworkDialog
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InventoryCompleteScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit
) {

    val generalState = appViewModel.generalState.value
    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    if (generalState.showNetworkDialog) {
        NetworkDialog(appViewModel = appViewModel, onClose = {
            appViewModel.onGeneralIntent(
                ShareIntent.ToggleNetworkDialog(false)
            )
        })
    }

    Layout(
        topBarText = Screen.InventoryComplete.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
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
            onNavigate(Screen.InventoryScan(Screen.Inventory.routeId))
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(30.dp)
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
                    .fillMaxWidth()
                    .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(10.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column {
                        listOf(
                            InventoryScanResultStatusModel(
                                status = InventoryScanResult.OK.displayName,
                                icon = R.drawable.scan_ok,
                                color = brightGreenPrimary
                            ),
                            InventoryScanResultStatusModel(
                                status = InventoryScanResult.SHORTAGE.displayName,
                                icon = R.drawable.scan_shortage,
                                color = brightOrange
                            ),
                            InventoryScanResultStatusModel(
                                status = InventoryScanResult.OVERLOAD.displayName,
                                icon = R.drawable.scan_overload,
                                color = Color(0xFFF44336)
                            ),
                            InventoryScanResultStatusModel(
                                status = InventoryScanResult.WRONG_LOCATION.displayName,
                                icon = R.drawable.scan_wrong_location,
                                color = deepBlueSky
                            ),
                        ).map {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(it.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(23.dp),
                                    tint = it.color
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    text = it.status, fontSize = 19.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    Column {
                        for (i in 1 until 5) {
                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "${i}件", fontSize = 19.sp
                            )
                        }
                    }
                }
            }
        }
    }
}