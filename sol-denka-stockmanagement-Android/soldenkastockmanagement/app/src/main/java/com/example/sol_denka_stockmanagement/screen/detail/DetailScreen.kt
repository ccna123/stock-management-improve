package com.example.sol_denka_stockmanagement.screen.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DetailScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onGoBack: () -> Unit,
) {

    val generalState by appViewModel.generalState.collectAsState()
    val rfidTagList by scanViewModel.rfidTagList.collectAsState()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
        appViewModel.onGeneralIntent(ShareIntent.ResetDetailIndex)
    }

    val currentItem =
        rfidTagList.filter { it.newFields.isChecked }.getOrNull(generalState.currentIndex)
    val totalCount = rfidTagList.filter { it.newFields.isChecked }.size

    Layout(
        topBarText = Screen.Detail.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Detail.routeId,
        prevScreenNameId = Screen.Detail.routeId,
        hasBottomBar = false,
        onBackArrowClick = { onGoBack() }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {

                // ===== CONTENT =====
                if (currentItem != null) {
                    ItemInfo(
                        title = stringResource(R.string.item_name_title),
                        content = currentItem.newFields.itemName
                    )
                    ItemInfo(
                        title = stringResource(R.string.item_code_title),
                        content = currentItem.newFields.itemCode
                    )
                    ItemInfo(
                        title = stringResource(R.string.location),
                        content = currentItem.newFields.location
                    )
                    ItemInfo(
                        title = stringResource(R.string.stock_status),
                        content = when {
                            currentItem.newFields.isInStock -> stringResource(R.string.in_stock)
                            else -> stringResource(R.string.not_in_stock)
                        },
                        textColor = when {
                            currentItem.newFields.isInStock -> brightGreenPrimary
                            else -> Color.Red
                        }
                    )
                } else {
                    Text(text = "No Data")
                }

                Spacer(modifier = Modifier.weight(1f))

                // ===== PAGINATION =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // PREV
                    Box(
                        modifier = Modifier
                            .clickable(enabled = generalState.currentIndex > 0) {
                                appViewModel.onGeneralIntent(ShareIntent.Prev)
                            }
                            .background(
                                color = if (generalState.currentIndex > 0)
                                    brightGreenSecondary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            tint = if (generalState.currentIndex > 0)
                                Color.White else Color.Black.copy(alpha = 0.2f)
                        )
                    }

                    // INDEX
                    Row {
                        Text(
                            fontSize = 26.sp,
                            text = if (totalCount == 0) "0"
                            else "${generalState.currentIndex + 1}"
                        )
                        Text(fontSize = 20.sp, text = "/")
                        Text(fontSize = 18.sp, text = totalCount.toString())
                    }

                    // NEXT
                    Box(
                        modifier = Modifier
                            .clickable(enabled = generalState.currentIndex < totalCount - 1) {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.Next(
                                        lastItemIndex = rfidTagList
                                            .filter { it.newFields.isChecked }
                                            .lastIndex
                                    )
                                )
                            }
                            .background(
                                color = if (generalState.currentIndex < totalCount - 1)
                                    brightGreenSecondary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            tint = if (generalState.currentIndex < totalCount - 1)
                                Color.White else Color.Black.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }

}
