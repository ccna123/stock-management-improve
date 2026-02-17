package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary

@Composable
fun LocationChangeSingleItem(
    tag: String,
    itemName: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    onClick: () -> Unit
) {
    CardContainer(
        isChecked = isChecked,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        onClick = onClick
    ) {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = brightGreenSecondary
            ),
            checked = isChecked,
            onCheckedChange = { onCheckedChange() }
        )
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = itemName, fontSize = 20.sp)
            Text(text = tag, fontSize = 16.sp)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}