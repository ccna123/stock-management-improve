package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable

fun InputContainer(
    title: String = "",
    isRequired: Boolean,
    children: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = title)
        if (isRequired) {
            Text(
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                text = "＊")
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    children()
}