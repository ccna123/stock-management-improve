package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import kotlinx.coroutines.launch

@Composable
fun CardContainer(
    isChecked: Boolean,
    isError: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
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

    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .shadow(
                elevation = 6.dp,
                shape = cardShape,
                clip = false
            )
            .background(Color.White, cardShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        // 👇 press down
                        scope.launch {
                            scale.animateTo(
                                0.9f,
                                animationSpec = tween(80)
                            )
                        }

                        try {
                            awaitRelease()
                        } finally {
                            // 👇 bounce back
                            scope.launch {
                                scale.animateTo(
                                    1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    },
                    onTap = {
                        onClick()
                    }
                )
            }
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
            verticalAlignment = verticalAlignment,
            horizontalArrangement = horizontalArrangement,
            modifier = Modifier
                .padding(start = 8.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            content = content
        )
    }
}