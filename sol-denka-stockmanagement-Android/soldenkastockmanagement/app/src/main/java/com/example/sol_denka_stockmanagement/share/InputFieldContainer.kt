package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.primaryRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputFieldContainer(
    modifier: Modifier = Modifier,
    value: String,
    hintText: String = "",
    errorMessages: List<String>? = null,
    isNumeric: Boolean = false,
    maxLength: Int? = null,
    currentLength: Int? = null,
    shape: Shape = RoundedCornerShape(13.dp),
    imeAction: ImeAction = ImeAction.Next,
    fontSize: TextUnit = 16.sp,
    isRequired: Boolean = false,
    enable: Boolean,
    label: String? = null,
    iconColor: Color = brightAzure,
    borderColor: Color = brightAzure,
    containerColor: Color = Color.Transparent,
    isDropDown: Boolean,
    readOnly: Boolean,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onChange: ((String) -> Unit)? = null,
    onEnterPressed: (() -> Unit)? = null,
) {
    val textFieldInteractionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val showSupporting = maxLength != null && currentLength != null
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            if (!readOnly) onChange?.invoke(newText)
        },
        supportingText = if (showSupporting) {
            {
                Text(
                    text = "$currentLength / $maxLength",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 11.sp,
                    color = if (currentLength > maxLength) primaryRed else brightAzure,
                )
            }
        } else null,
        modifier = modifier,
        shape = shape,
        label = label?.let {
            {
                Text(
                    text = if (isRequired) "$it＊" else it,
                    color = if (isRequired) Color.Red else Color.Black,
                )
            }
        },
        placeholder = { Text(text = hintText, color = Color.Gray, fontSize = 16.sp) },
        interactionSource = textFieldInteractionSource,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =
                if (!errorMessages.isNullOrEmpty()) Color.Red else borderColor,
            unfocusedBorderColor = if (!errorMessages.isNullOrEmpty()) Color.Red else borderColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = Color.White,
            disabledBorderColor = brightAzure,
            disabledTextColor = Color.Black
        ),
        singleLine = singleLine,
        textStyle = TextStyle(fontSize = fontSize),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text,
            imeAction = if (singleLine) imeAction else ImeAction.Default
        ),
        keyboardActions =
            if (singleLine) {
                KeyboardActions(
                    onNext = {
                        if (!readOnly) {
                            onEnterPressed?.invoke()
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                )
            } else {
                KeyboardActions(
                    onAny = {
                        // Do nothing -> allow newline
                    }
                )
            },
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
            } else {
                trailingIcon?.invoke()
            }
        }
    )
}