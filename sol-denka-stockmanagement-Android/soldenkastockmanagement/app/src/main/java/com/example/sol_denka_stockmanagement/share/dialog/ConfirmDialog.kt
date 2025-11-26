package com.example.sol_denka_stockmanagement.share.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    dialogTitle: String,
    buttons: List<@Composable () -> Unit>,
) {
    if (showDialog.not()) return
    AppDialog {
        Text(
            text = dialogTitle,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            buttons.forEach { button ->
                button.invoke()
            }
        }
    }
}