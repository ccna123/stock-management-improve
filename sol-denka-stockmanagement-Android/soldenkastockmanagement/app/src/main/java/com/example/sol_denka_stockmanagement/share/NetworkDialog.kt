package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@Composable
fun NetworkDialog(appViewModel: AppViewModel, onClose: () -> Unit) {
    AppDialog {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.network_connect_failed),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))
            Row {
                ButtonContainer(
                    shape = RoundedCornerShape(10.dp),
                    buttonTextSize = 20,
                    containerColor = Color.Red,
                    onClick = {
                        onClose()
                    },
                    buttonText = stringResource(R.string.close),
                )
            }
        }
    }
}