package com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingBoxContainer(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            children()
        }
    }
}
