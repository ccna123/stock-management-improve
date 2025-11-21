package com.example.sol_denka_stockmanagement.share

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
    Row(modifier = Modifier.fillMaxWidth()) {

        SingleTab(
            text = leftTabText,
            isSelected = tab == leftTab,
            icon = leftIcon,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .offset(x = 10.dp)
                .zIndex(if (tab == leftTab) 2f else 1f),
            onClick = { onChangeTab(leftTab) }
        )

        SingleTab(
            text = rightTabText,
            isSelected = tab == rightTab,
            icon = rightIcon,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .offset(x = (-10).dp)
                .zIndex(if (tab == rightTab) 2f else 1f),
            onClick = { onChangeTab(rightTab) }
        )
    }
}