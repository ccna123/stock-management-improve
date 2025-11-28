package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.TableCell(content: String, weight: Float, contentSize: TextUnit = 16.sp) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .border(.5.dp, color = Color.Black.copy(alpha = 0.3f))
            .weight(weight),
        contentAlignment = Alignment.Center
    ) {
        Text(text = content, fontSize = contentSize, modifier = Modifier.padding(8.dp))
    }
}