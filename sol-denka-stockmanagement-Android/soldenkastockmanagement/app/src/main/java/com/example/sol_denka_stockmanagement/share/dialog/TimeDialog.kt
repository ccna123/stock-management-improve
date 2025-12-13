package com.example.sol_denka_stockmanagement.share.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    showTimeDialog: Boolean,
    title: String,
    confirmText: String,
    cancelText: String,
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showTimeDialog.not()) return
    val current = Calendar.getInstance()
    val state = rememberTimePickerState(
        initialHour = current.get(Calendar.HOUR_OF_DAY),
        initialMinute = current.get(Calendar.MINUTE),
        is24Hour = true
    )

    TimePickerDialog(
        title = { Text(text = title) },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val timeStr = String.format(Locale.US, "%02d:%02d", state.hour, state.minute)
                    onConfirm(timeStr)
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = cancelText)
            }
        }
    ) {
        TimePicker(state = state)
    }
}