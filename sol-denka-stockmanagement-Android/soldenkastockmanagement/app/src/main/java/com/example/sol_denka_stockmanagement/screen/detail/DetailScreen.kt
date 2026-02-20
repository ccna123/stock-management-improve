package com.example.sol_denka_stockmanagement.screen.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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

    val checkedList = remember(rfidTagList) {
        rfidTagList.filter { it.newFields.isChecked }
    }

    val currentItem = checkedList.getOrNull(generalState.currentIndex)
    val totalCount = checkedList.size

    Layout(
        topBarText = Screen.Detail.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        hasBottomBar = false,
        onBackArrowClick = { onGoBack() }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // ===== CONTENT =====
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {

                    if (currentItem != null) {

                        val fields = currentItem.newFields

                        ItemInfo(stringResource(R.string.item_name_title), fields.itemName)

                        ItemInfo(stringResource(R.string.item_category_name), fields.categoryName)

                        ItemInfo(stringResource(R.string.location), fields.location)

                        ItemInfo(
                            title = stringResource(R.string.item_stock_status),
                            content = if (fields.isInStock)
                                stringResource(R.string.in_stock)
                            else
                                stringResource(R.string.not_in_stock),
                            textColor = if (fields.isInStock)
                                brightGreenPrimary
                            else
                                Color.Red
                        )

                        ItemInfo(
                            stringResource(R.string.item_packing_type),
                            fields.packingType.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.specific_gravity),
                            fields.specificGravity.toString()
                        )

                        ItemInfo(
                            stringResource(R.string.thickness),
                            fields.thickness?.stripTrailingZeros()?.toPlainString().orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.width),
                            fields.width?.toString().orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.length),
                            fields.length?.toString().orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.quantity),
                            fields.quantity?.toString().orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.weight),
                            fields.weight?.toString().orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.item_winder),
                            fields.winderName.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.lot_no),
                            fields.lotNo.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.occurrenceReason),
                            fields.occurrenceReason.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.occurred_at_date_time),
                            fields.occurredAt.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.processed_at_date_time),
                            fields.processedAt.orEmpty()
                        )

                        ItemInfo(
                            stringResource(R.string.memo),
                            currentItem.memo.orEmpty()
                        )

                    } else {
                        Text(text = "No Data")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== PAGINATION =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                PaginationButton(
                    enabled = generalState.currentIndex > 0,
                    icon = Icons.Filled.ArrowBackIosNew
                ) {
                    appViewModel.onGeneralIntent(ShareIntent.Prev)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        fontSize = 26.sp,
                        text = if (totalCount == 0)
                            "0"
                        else
                            "${generalState.currentIndex + 1}"
                    )
                    Text(fontSize = 20.sp, text = "/")
                    Text(fontSize = 18.sp, text = totalCount.toString())
                }

                PaginationButton(
                    enabled = generalState.currentIndex < totalCount - 1,
                    icon = Icons.AutoMirrored.Filled.ArrowForwardIos
                ) {
                    appViewModel.onGeneralIntent(
                        ShareIntent.Next(
                            lastItemIndex = checkedList.lastIndex
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PaginationButton(
    enabled: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(enabled = enabled) { onClick() }
            .background(
                color = if (enabled) brightGreenSecondary else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(10.dp),
            tint = if (enabled)
                Color.White
            else
                Color.Black.copy(alpha = 0.2f)
        )
    }
}