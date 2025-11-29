package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.constant.HandlingMethod
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingSingleItem(
    tag: String,
    isChecked: Boolean,
    isExpanded: Boolean,
    value: String,
    onSelect: () -> Unit,
    onExpandedChange: () -> Unit,
    onCheckedChange: () -> Unit,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onClickDropDownMenuItem: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = { onSelect() })
        ) {
            Checkbox(
                colors = CheckboxDefaults.colors(
                    checkedColor = brightGreenSecondary
                ),
                checked = isChecked,
                onCheckedChange = { onCheckedChange() }
            )
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = MaterialSelectionItem.MISS_ROLL.displayName)
                Text(text = tag)
            }
        }
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange() }) {
            InputFieldContainer(
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth(),
                value = value,
                isNumeric = false,
                hintText = SelectTitle.SelectHandlingMethod.displayName,
                onChange = { newValue -> onValueChange(newValue) },
                readOnly = true,
                isDropDown = true,
                enable = true,
                iconColor = brightAzure,
//                onClick = { onClickInput() },
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onDismissRequest() }
            ) {
                listOf(
                    SelectTitle.SelectHandlingMethod.displayName,
                    HandlingMethod.USE.displayName,
                    HandlingMethod.SALE.displayName,
                    HandlingMethod.CRUSHING.displayName,
                ).forEach { method ->
                    DropdownMenuItem(
                        text = { Text(text = method) },
                        onClick = { onClickDropDownMenuItem(method) }
                    )
                }
            }
        }
    }
}