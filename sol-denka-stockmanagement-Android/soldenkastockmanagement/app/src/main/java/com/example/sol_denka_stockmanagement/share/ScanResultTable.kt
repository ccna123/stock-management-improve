package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.model.scan.ScanResultRowModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue

@Composable
fun ScanResultTable(
    tableHeight: Dp = 250.dp,
    scanResult: List<ScanResultRowModel>,
    tableHeader: List<String>
) {
    val corner = 14.dp

    Column {

        // ---------- HEADER ----------
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
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            tableHeader.forEachIndexed { index, title ->

                val shape = when (index) {
                    0 -> RoundedCornerShape(topStart = corner)
                    tableHeader.lastIndex -> RoundedCornerShape(topEnd = corner)
                    else -> RoundedCornerShape(0.dp)
                }

                TableCell(
                    content = title,
                    weight = 1f,
                    contentSize = 13.sp,
                    shape = shape,
                    textColor = Color.White
                )
            }
        }

        // ---------- BODY ----------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(tableHeight)
                .border(
                    1.dp,
                    brightAzure,
                    RoundedCornerShape(bottomStart = corner, bottomEnd = corner)
                )
                .background(
                    Color.White,
                    RoundedCornerShape(bottomStart = corner, bottomEnd = corner)
                )
        ) {
            items(scanResult) { row ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Cell 1
                    TableCell(
                        content = row.itemName,
                        weight = 1f
                    )

                    // Cell 2
                    TableCell(
                        content = row.itemCode,
                        weight = 1f
                    )

                    // Cell 3
                    TableCell(
                        content = row.lastColumn,
                        weight = 1f
                    )
                }
            }
        }
    }
}
