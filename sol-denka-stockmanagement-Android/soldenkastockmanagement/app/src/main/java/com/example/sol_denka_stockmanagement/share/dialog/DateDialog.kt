package com.example.sol_denka_stockmanagement.share.dialog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.util.Calendar
import java.util.Locale

@Composable
fun DateDialog(
    showDateDialog: Boolean,
    confirmText: String,
    cancelText: String,
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showDateDialog.not()) return
    val today = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = today.timeInMillis
    )

    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val cal = Calendar.getInstance().apply { timeInMillis = millis }
                        val dateStr = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH)
                        )
                        onConfirm(dateStr)
                    }
                    onDismissRequest()
                }
            ) { Text(text = confirmText) }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) { Text(text = cancelText) }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}