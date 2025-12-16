package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary
import com.example.sol_denka_stockmanagement.ui.theme.primaryRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutboundSingleItem(
    processTypeList: List<ProcessTypeModel>,
    tag: String,
    itemName: String,
    isChecked: Boolean,
    isExpanded: Boolean,
    isError: Boolean,
    value: String,
    onSelect: () -> Unit,
    onExpandedChange: () -> Unit,
    onCheckedChange: () -> Unit,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onClickDropDownMenuItem: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                brush = when {
                    isError -> Brush.horizontalGradient(
                        colors = listOf(
                            primaryRed.copy(alpha = 0.25f),
                            primaryRed.copy(alpha = 0.22f)
                        )
                    )

                    isChecked -> Brush.horizontalGradient(
                        colors = listOf(
                            brightGreenSecondary.copy(alpha = 0.25f),
                            brightGreenSecondary.copy(alpha = 0.20f)
                        )
                    )

                    else -> Brush.linearGradient(
                        listOf(Color.White, Color.White)
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp, color = when {
                    isError -> primaryRed
                    isChecked -> brightGreenPrimary
                    else -> brightAzure
                }, shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = { onSelect() })
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
            Text(text = itemName)
            Text(text = tag)
            ExposedDropdownMenuBox(
                modifier = Modifier.wrapContentSize(),
                expanded = isExpanded,
                onExpandedChange = { onExpandedChange() }) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    value = value,
                    isNumeric = false,
                    hintText = SelectTitle.SelectProcessMethod.displayName,
                    onChange = { newValue -> onValueChange(newValue) },
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    iconColor = brightAzure,
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { onDismissRequest() }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = SelectTitle.SelectProcessMethod.displayName) },
                        onClick = { onClickDropDownMenuItem("") }
                    )
                    processTypeList.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(text = method.processName) },
                            onClick = { onClickDropDownMenuItem(method.processName) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}