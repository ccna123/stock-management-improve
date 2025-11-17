package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.intent.ShareIntent

@Composable
fun ShippingSingleItem(
    tag: String,
    isChecked: Boolean,
    onSelect: () -> Unit,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onSelect() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = MaterialSelectionItem.MISS_ROLL.displayName)
            Text(text = tag)
        }
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange() }
        )
    }
}