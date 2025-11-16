package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.ui.theme.brightGreen

@Composable
fun ConnectionStatusRow(
    label: String,
    isConnected: Boolean,
    connectedText: String,
    connectedIcon: ImageVector,
    disconnectedText: String,
    disconnectedIcon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isConnected) connectedIcon else disconnectedIcon,
                contentDescription = null,
                tint = if (isConnected) brightGreen else Color.Red,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isConnected) connectedText else disconnectedText,
                fontWeight = FontWeight.Bold,
                color = if (isConnected) brightGreen else Color.Red
            )
        }
    }
}