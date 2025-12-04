package com.example.sol_denka_stockmanagement.helper.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ToastMessage(
    modifier: Modifier,
    style: TextStyle,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp)
) {
    val toastMessage by ToastManager.toastMessage.collectAsState()
    val showMessage by ToastManager.showMessage.collectAsState()
    val type by ToastManager.type.collectAsState()

    LaunchedEffect(showMessage) {
        delay(2000) // Hide after 2 seconds
        ToastManager.hideToast()
    }
    if (showMessage) {
        val backgroundColor = when (type) {
            ToastType.SUCCESS -> Color(0xff53cf74)
            ToastType.INFO -> Color(0xffB2E5F1)
            ToastType.ERROR -> Color(0xffFFDAD6)
            ToastType.WARNING -> Color(0xffFFE8B2)
        }

        val iconColor = when (type) {
            ToastType.SUCCESS -> Color(0xFF2F6A43) // Dark green, matches success background (0xff53cf74)
            ToastType.INFO -> Color(0xFF2F5A6A)    // Dark teal, complements info background (0xffB2E5F1)
            ToastType.ERROR -> Color(0xFF8B1A1A)   // Dark red, complements error background (0xffFFDAD6)
            ToastType.WARNING -> Color(0xFF6A4B2F) // Dark brown, complements warning background (0xffFFE8B2)
        }

        val icon = when (type) {
            ToastType.SUCCESS -> Icons.Default.CheckCircle
            ToastType.INFO -> Icons.Default.Info
            ToastType.ERROR -> Icons.Default.Close
            ToastType.WARNING -> Icons.Default.Warning
        }

        val textColor = when (type) {
            ToastType.SUCCESS -> Color(0xFF1A3C24) // Darker green for light green background
            ToastType.INFO -> Color(0xFF1A3C4D)    // Darker teal for light blue background
            ToastType.ERROR -> Color(0xFF4D1A1A)   // Darker red for light red background
            ToastType.WARNING -> Color(0xFF4D3C1A) // Darker brown for light yellow background
        }

        Box(
            modifier = modifier
                .background(backgroundColor, shape)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    style = style.copy(color = textColor),
                    text = toastMessage
                )

            }
        }
    }
}

enum class ToastType {
    SUCCESS,
    INFO,
    ERROR,
    WARNING
}