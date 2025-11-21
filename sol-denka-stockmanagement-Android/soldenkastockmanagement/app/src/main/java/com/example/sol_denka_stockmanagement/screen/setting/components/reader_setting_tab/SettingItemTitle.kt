package com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingItemTitle(text: String, color: Color = Color.Black, textSize: TextUnit = 16.sp) {
    Text(
        text = text,
        fontSize = textSize,
        color = color,
        modifier = Modifier.padding(8.dp)
    )
}