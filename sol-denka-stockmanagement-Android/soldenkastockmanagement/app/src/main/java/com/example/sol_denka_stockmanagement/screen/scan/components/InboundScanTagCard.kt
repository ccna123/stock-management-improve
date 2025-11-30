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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun InboundScanTagCard(epc: String, itemName: String, itemCode: String) {
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
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                            text = stringResource(R.string.scanned_tag),
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = epc,
                        fontSize = 18.sp,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.item_code),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFF5C6BC0)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.tariff_code),
                            fontSize = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = itemCode,
                        fontSize = 17.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.item_name),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFF8D8D8D)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.item_name_title),
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = itemName,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFFFF9800)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.read_timestamp),
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (epc.isEmpty()) "-" else LocalTime.now()
                            .format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}