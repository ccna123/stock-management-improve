package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary

@Composable
fun LocationChangeSingleItem(
    tag: String,
    itemName: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = itemName)
            Text(text = tag)
        }
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = brightGreenSecondary
            ),
            checked = isChecked,
            onCheckedChange = { onCheckedChange() }
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider(color = brightAzure)
    Spacer(modifier = Modifier.height(10.dp))
}