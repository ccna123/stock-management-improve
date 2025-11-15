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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanViewModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DetailScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inventoryScanViewModel: InventoryScanViewModel,
    detailViewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigate: (Screen) -> Unit
) {

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
        detailViewModel.setItems(inventoryScanViewModel.selectedTags.value)
    }

    val selectedList by detailViewModel.selectedList.collectAsStateWithLifecycle()
    val currentIndex by detailViewModel.currentIndex.collectAsStateWithLifecycle()
    val currentItem = selectedList.getOrNull(currentIndex)
    val totalCount = selectedList.size

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
        DetailScreenContent(
            modifier = Modifier.padding(paddingValues),
            item = currentItem,
            currentIndex = currentIndex,
            totalCount = totalCount,
            onPrev = { detailViewModel.handle(DetailIntent.Prev) },
            onNext = { detailViewModel.handle(DetailIntent.Next) }
        )
    }
}

@Composable
fun DetailScreenContent(
    modifier: Modifier,
    item: InventoryItemMasterModel?,
    currentIndex: Int,
    totalCount: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {

    val canPrev = currentIndex > 0
    val canNext = currentIndex < totalCount - 1

    LazyColumn(
        modifier = modifier
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            if (item != null) {
                Text(text = "スキャンタグ: ${item.epc}")
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
                    tint = if (canPrev) brightAzure else Color.LightGray,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(enabled = canPrev) { onPrev() },
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null
                )

                Row {
                    Text(fontSize = 26.sp, text = if (totalCount == 0) "0" else "${currentIndex + 1}")
                    Text(fontSize = 20.sp, text = "/")
                    Text(fontSize = 18.sp, text = totalCount.toString())
                }

                Icon(
                    tint = if (canNext) brightAzure else Color.LightGray,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(enabled = canNext) { onNext() },
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }
}
