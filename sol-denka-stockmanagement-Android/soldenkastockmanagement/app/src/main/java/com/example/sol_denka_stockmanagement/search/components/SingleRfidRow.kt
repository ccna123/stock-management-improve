package com.example.sol_denka_stockmanagement.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.ui.theme.deepOceanBlue

@Composable
fun SingleRfidRow(
    boxWidth: Dp,
    onChange: () -> Unit,
    isPressed: Boolean? = false,
    rfidNo: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.width(90.dp),
            text = rfidNo,
            fontSize = 19.sp,
            style = TextStyle(lineHeight = 24.sp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                .width(140.dp)
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = brightAzure,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .width(boxWidth)
                    .height(40.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f)) // Push Button to the right
        Button(
            onClick = { onChange() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPressed == true) brightGreenSecondary else Color.White,
                contentColor = brightAzure
            ),
            border = BorderStroke(1.dp, if (isPressed == true) Color.Transparent else brightAzure)
        ) {
            Text(
                fontSize = 16.sp,
                color = if (isPressed == true) Color.White else deepOceanBlue,
                text = stringResource(R.string.found)
            )
        }
    }
}
