package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.model.ScanResultRowModel
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue

@Composable
fun ScanResultTable(tableHeight: Dp = 150.dp, scanResult: List<ScanResultRowModel>, tableHeader: List<String>) {
    Column{
        val localTempFontSize = compositionLocalOf { 13.sp }
        CompositionLocalProvider(localTempFontSize provides localTempFontSize.current) {
            Row(
                modifier = Modifier
                    .background(color = paleSkyBlue)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tableHeader.map { tableHeader ->
                    TableCell(
                        content = tableHeader,
                        contentSize = localTempFontSize.current,
                        weight = 1f
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .height(tableHeight)
        ) {
            items(scanResult) { tag ->
                Row(
                    modifier = Modifier
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