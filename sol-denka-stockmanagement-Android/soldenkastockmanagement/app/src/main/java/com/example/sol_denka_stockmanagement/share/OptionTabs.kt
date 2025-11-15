package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.constant.Tab

@Composable
fun OptionTabs(
    tab: Tab,
    leftTabText: String,
    rightTabText: String,
    leftTab: Tab,
    rightTab: Tab,
    leftIcon: ImageVector,
    rightIcon: ImageVector,
    onChangeTab: (Tab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SingleTab(
            text = leftTabText,
            isSelected = tab == leftTab,
            modifier = Modifier.weight(1f),
            icon = leftIcon,
            onClick = { onChangeTab(leftTab) },
        )
        SingleTab(
            text = rightTabText,
            isSelected = tab == rightTab,
            modifier = Modifier.weight(1f),
            icon = rightIcon,
            onClick = { onChangeTab(rightTab) },
        )
    }
}