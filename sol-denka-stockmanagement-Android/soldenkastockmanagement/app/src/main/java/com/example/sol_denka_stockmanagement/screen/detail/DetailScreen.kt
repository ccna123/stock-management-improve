package com.example.sol_denka_stockmanagement.screen.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DetailScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit
) {

    val generalState = appViewModel.generalState.value

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    val currentItem = generalState.selectedTags.getOrNull(generalState.currentIndex)
    val totalCount = generalState.selectedTags.size

    Layout(
        topBarText = Screen.Detail.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Detail.routeId,
        prevScreenNameId = Screen.Detail.routeId,
        hasBottomBar = false,
        onBackArrowClick = {
            onNavigate(Screen.InventoryScan(""))
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
                    Icon(
                        tint = if (generalState.currentIndex > 0) brightAzure else Color.LightGray,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable(enabled = generalState.currentIndex > 0) {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.Prev
                                )
                            },
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null
                    )

                    Row {
                        Text(
                            fontSize = 26.sp,
                            text = if (totalCount == 0) "0" else "${generalState.currentIndex + 1}"
                        )
                        Text(fontSize = 20.sp, text = "/")
                        Text(fontSize = 18.sp, text = totalCount.toString())
                    }

                    Icon(
                        tint = if (generalState.currentIndex < totalCount - 1) brightAzure else Color.LightGray,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable(enabled = generalState.currentIndex < totalCount - 1) {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.Next
                                )
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
