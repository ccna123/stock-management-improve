package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary

@Composable
fun CardContainer(
    isChecked: Boolean,
    isError: Boolean = false,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val cardShape = if (isChecked || isError) {
        RoundedCornerShape(
            topStart = 6.dp,
            bottomStart = 6.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )
    } else {
        RoundedCornerShape(16.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = cardShape,
                clip = false
            )
            .background(Color.White, cardShape)
            .clickable { onClick() }
    ) {

        /* LEFT BORDER – ONLY WHEN CHECKED */
        if (isChecked || isError) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (isError) Color.Red else brightGreenSecondary,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
            )
        } else {
            Spacer(modifier = Modifier.width(6.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 8.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            content = content
        )
    }
}