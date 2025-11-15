package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel

@Composable
fun ScannedTagDisplay(
    rfidTagList: List<InventoryItemMasterModel>,
    selectedTags: List<InventoryItemMasterModel>,
    isSelectionMode: Boolean,
    onClick: (InventoryItemMasterModel) -> Unit,
    onLongClick: (InventoryItemMasterModel) -> Unit,
    onCheckedChange : (InventoryItemMasterModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .border(
                1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(rfidTagList) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            onClick(item)
                        },
                        onLongClick = {
                            onLongClick(item)
                        }
                    )
            ) {
                if (isSelectionMode) {
                    Checkbox(
                        checked = item in selectedTags,
                        onCheckedChange = {
                            onCheckedChange(item)
                        }
                    )
                }
                Column {
                    Text(text = "一斗缶", fontSize = 17.sp)
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp),
                        text = item.epc, fontSize = 25.sp
                    )
                }
            }
            HorizontalDivider()
        }
    }
}