package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    content: String,
    weight: Float,
    textColor: Color = Color.Black,
    contentSize: TextUnit = 16.sp,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp)
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .weight(weight)
            .clip(shape)
            .border(
                width = 0.6.dp,
                color = Color.LightGray.copy(alpha = .4f),
                shape = shape
            )
            .padding(vertical = 10.dp)
            .horizontalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(text = content, fontSize = contentSize, color = textColor)
    }
}
