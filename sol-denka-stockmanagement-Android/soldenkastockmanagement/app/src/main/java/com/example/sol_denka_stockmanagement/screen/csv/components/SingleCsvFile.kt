package com.example.sol_denka_stockmanagement.screen.csv.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary

@Composable
fun SingleCsvFile(
    modifier: Modifier = Modifier,
    csvFileName: String,
    csvFileSize: String,
    progress: Float = 0f,
    isCompleted: Boolean = false,
    isError: Boolean = false,
    showProgress: Boolean = false,
    isSelected: Boolean = false,
    onChoose: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(vertical = 0.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(color = if (isSelected) brightGreenPrimary.copy(alpha = 0.1f) else Color.White, shape = RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) brightGreenPrimary else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = { onChoose() }
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ==== ICON WRAPPER ====
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = Color(0xFFD9F7D3),   // light pastel green
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    color = Color(0xFF8AD882),    // bright green
                    shape = RoundedCornerShape(12.dp)
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.csv),  // SVG with full color
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }

        // ==== TEXT INFO ====
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = csvFileName,
                color = Color.Black
            )

            Text(
                text = "Size  $csvFileSize",
                color = Color.Gray
            )

            if (showProgress) {
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .height(6.dp)
                            .weight(1f),
                        color = brightGreenPrimary,
                        trackColor = Color.LightGray
                    )

                    Text(text = "${(progress * 100).toInt()}%")

                    when {
                        isCompleted -> Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = brightGreenPrimary
                        )

                        isError -> Icon(
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
