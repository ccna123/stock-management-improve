package com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingItemTitle(text: String, color: Color = Color.Black) {
    Text(
        text = text,
        color = color,
        modifier = Modifier.padding(8.dp)
    )
}