package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure

enum class ButtonPosition {
    Top,
    Bottom,
    Unknown
}


@Composable
fun ButtonContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = brightAzure,
    borderColor: Color = Color.Unspecified,
    onClick: () -> Unit,
    canClick: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    icon: @Composable (() -> Unit)? = null,
    buttonHeight: Dp = 48.dp,
    position: ButtonPosition = ButtonPosition.Bottom,
    buttonText: String = "",
    buttonTextSize: Int = 16,
    textColor: Color = Color.White,
) {
    Button(
        modifier = modifier
            .width(125.dp)
            .height(buttonHeight)
            .border(
                1.dp,
                color = if (canClick) borderColor else Color.Unspecified,
                shape = shape
            ),
        onClick = { onClick() },
        enabled = canClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = Color.LightGray
        ),
        shape = shape,
        contentPadding = PaddingValues(2.dp)
    ) {
        if (icon != null) {
            icon.invoke()
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = buttonText,
            color = if (canClick) textColor else Color.White,
            fontSize = buttonTextSize.sp
        )
    }
}