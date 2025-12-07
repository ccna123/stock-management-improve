package com.example.sol_denka_stockmanagement.screen.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
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
        onBackArrowClick = {
            onGoBack()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (currentItem != null) {
                    Text(text = "スキャンタグ: ${currentItem.epc}")
                } else {
                    Text(text = "No Data")
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(enabled = generalState.currentIndex > 0) {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.Prev
                                )
                            }
                            .background(
                                color = if (generalState.currentIndex > 0) brightGreenSecondary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            tint = if (generalState.currentIndex > 0) Color.White else Color.Black.copy(alpha = .2f),
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                    Row {
                        Text(
                            fontSize = 26.sp,
                            text = if (totalCount == 0) "0" else "${generalState.currentIndex + 1}"
                        )
                        Text(fontSize = 20.sp, text = "/")
                        Text(fontSize = 18.sp, text = totalCount.toString())
                    }
                    Box(
                        modifier = Modifier
                            .clickable(enabled = generalState.currentIndex < totalCount - 1) {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.Next(lastItemIndex = rfidTagList.filter { it.newFields.isChecked }.lastIndex)
                                )
                            }
                            .background(
                                color = if (generalState.currentIndex < totalCount - 1) brightGreenSecondary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ){
                        Icon(
                            tint = if (generalState.currentIndex < totalCount - 1) Color.White else Color.Black.copy(alpha = .2f),
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
