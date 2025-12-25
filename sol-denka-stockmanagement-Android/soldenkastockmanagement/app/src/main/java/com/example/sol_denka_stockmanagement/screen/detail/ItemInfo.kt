package com.example.sol_denka_stockmanagement.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemInfo(title: String, content: String, textColor: Color = Color.Black) {
    Column {
        Text(
            fontSize = 20.sp,
            text = title,
        )
        Text(
            color = textColor,
            text = content
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider()
}