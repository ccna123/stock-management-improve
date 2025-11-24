package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ReceivingScanTagCard(scannedTag: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                // ✨ Add the shadow first, with clip disabled so the shadow can extend beyond the shape
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false, // 👈 allow the shadow to bleed outside the box
                )
                // 💡 Then draw the background after the shadow
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                // Add border for your pale outline
                .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    text = "読み取りタグ",
                    fontSize = 20.sp
                )
                HorizontalDivider(
                    color = brightAzure,
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.tag),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = deepBlueSky
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = scannedTag.takeIf { it.isNotEmpty() }
                            ?: "まだタグが読み取られていません",
                        fontSize = 17.sp,
                        color = if (scannedTag.isEmpty()) Color.Red else Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                // ✅ Timestamp row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFF616161)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "読み取り日時：${
                            if (scannedTag.isEmpty()) "" else LocalTime.now()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        }",
                        fontSize = 17.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}