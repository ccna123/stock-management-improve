package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.sol_denka_stockmanagement.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure

@Composable
fun ScanResultTable(
    column1Weight: Float = 1f,
    column2Weight: Float = 1f,
    column3Weight: Float = 1f,
    tableHeight: Dp = 250.dp,
    scanResult: List<ScanResultRowModel>,
    tableHeader: List<String>
) {
    val corner = 14.dp

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brightAzure,
                    RoundedCornerShape(topStart = corner, topEnd = corner)
                )
                .border(
                    1.dp,
                    brightAzure,
                    RoundedCornerShape(topStart = corner, topEnd = corner)
                )
        ) {
            tableHeader.forEachIndexed { index, title ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(topStart = corner)
                    tableHeader.lastIndex -> RoundedCornerShape(topEnd = corner)
                    else -> RoundedCornerShape(0)
                }

                TableCell(
                    content = title,
                    weight = when (index) {
                        0 -> column1Weight
                        1 -> column2Weight
                        else -> column3Weight
                    },
                    contentSize = 13.sp,
                    shape = shape,
                    textColor = Color.White
                )
            }
        }

        // >>>>>>>>>>>>>>> NEW LOGIC: detect if scrollable <<<<<<<<<<<<<<<<
        val scrollable =
            scanResult.size * 48.dp > tableHeight

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(tableHeight)
                .border(
                    1.dp,
                    brightAzure,
                    RoundedCornerShape(
                        bottomStart = corner,
                        bottomEnd = corner
                    )
                )
                .clip(
                    RoundedCornerShape(
                        bottomStart = corner,
                        bottomEnd = corner
                    )
                )
                .background(Color.White)
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                itemsIndexed(scanResult) { index, row ->

                    val isLast = index == scanResult.lastIndex

                    // >>>>>>> ONLY ROUND BOTTOM WHEN SCROLLABLE <<<<<<<
                    val leftShape =
                        if (isLast && scrollable) RoundedCornerShape(bottomStart = corner)
                        else RoundedCornerShape(0)

                    val rightShape =
                        if (isLast && scrollable) RoundedCornerShape(bottomEnd = corner)
                        else RoundedCornerShape(0)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {

                        TableCell(
                            content = row.itemName,
                            shape = leftShape,
                            weight = column1Weight
                        )

                        TableCell(
                            content = row.itemCode,
                            shape = RoundedCornerShape(0),
                            weight = column2Weight
                        )

                        TableCell(
                            content = row.lastColumn,
                            shape = rightShape,
                            weight = column3Weight
                        )
                    }
                }
            }
        }
    }
}


