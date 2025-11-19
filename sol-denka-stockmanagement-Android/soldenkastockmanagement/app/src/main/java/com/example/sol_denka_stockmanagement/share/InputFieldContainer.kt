package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import androidx.compose.ui.graphics.Shape // Correct Shape import
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputFieldContainer(
    modifier: Modifier = Modifier,
    value: String,
    hintText: String = "",
    error: Boolean? = false,
    isNumeric: Boolean = false,
    shape: Shape = RoundedCornerShape(13.dp),
    fontSize: TextUnit = 16.sp,
    enable: Boolean,
    label: String? = null,
    iconColor: Color = brightAzure,
    isDropDown: Boolean,
    readOnly: Boolean,
    singleLine: Boolean = true,
    onChange: ((String) -> Unit)? = null,
    onEnterPressed: (() -> Unit)? = null,
) {
    val textFieldInteractionSource = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            if (!readOnly) onChange?.invoke(newText.replace("\n", ""))
        },
        modifier = modifier,
        shape = shape,
        label = label?.let { { Text(text = it) } },
        placeholder = { Text(text = hintText, color = Color.Gray, fontSize = 16.sp) },
        interactionSource = textFieldInteractionSource,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (error == true) Color.Red else brightAzure,
            unfocusedBorderColor = if (error == true) Color.Red else brightAzure,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.LightGray.copy(alpha = 0.4f),
            disabledBorderColor = Color.LightGray.copy(alpha = 0.4f),
            disabledTextColor = Color.Black
        ),
        singleLine = singleLine,
        textStyle = TextStyle(fontSize = fontSize),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text,
            imeAction = if (readOnly) ImeAction.None else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { if (!readOnly) onEnterPressed?.invoke() }),
        readOnly = readOnly,
        enabled = enable,
        trailingIcon = {
            if (isDropDown) {
                Icon(
                    modifier = Modifier.size(45.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = iconColor
                )
            }
        }
    )
}