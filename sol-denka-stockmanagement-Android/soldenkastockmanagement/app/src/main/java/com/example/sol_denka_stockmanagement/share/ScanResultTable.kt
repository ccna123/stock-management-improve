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
import com.example.sol_denka_stockmanagement.model.ScanResultRowModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue

@Composable
fun ScanResultTable(tableHeight: Dp = 250.dp, scanResult: List<ScanResultRowModel>, tableHeader: List<String>) {
    Column{
            Row(
                modifier = Modifier
                    .border(1.dp, color = brightAzure)
                    .background(color = paleSkyBlue)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tableHeader.map { tableHeader ->
                    TableCell(
                        content = tableHeader,
                        contentSize = 13.sp,
                        weight = 1f
                    )
            }
        }
        LazyColumn(
            modifier = Modifier
                .border(1.dp, color = brightAzure)
                .height(tableHeight)
        ) {
            items(scanResult) { tag ->
                Row(
                    modifier = Modifier
                        .border(1.dp, color = Color.LightGray)
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableCell(
                        content = tag.itemName,
                        weight = 1f
                    )
                    TableCell(
                        content = tag.itemCode,
                        weight = 1f
                    )
                    TableCell(
                        content = tag.lastColumn,
                        weight = 1f
                    )
                }
            }
        }
    }
}