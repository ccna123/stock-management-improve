package com.example.sol_denka_stockmanagement.screen.csv.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary

@Composable
fun SingleCsvFile(
    modifier: Modifier,
    csvFileName: String,
    csvFileSize: String,
    progress: Float = 0f,
    index: Int,
    isCompleted: Boolean = false,
    isError: Boolean = false,
    showProgress: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(if (index % 2 == 0) 10.dp else 0.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = csvFileName
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Size")
            Text(text = csvFileSize)
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (showProgress) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LinearProgressIndicator(
                    progress = {
                        progress
                    },
                    modifier = Modifier
                        .height(6.dp),
                )
                Text(text = "${(progress * 100).toInt()}%")
                when {
                    isCompleted -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = brightGreenPrimary
                        )
                    }
                    isError -> {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}