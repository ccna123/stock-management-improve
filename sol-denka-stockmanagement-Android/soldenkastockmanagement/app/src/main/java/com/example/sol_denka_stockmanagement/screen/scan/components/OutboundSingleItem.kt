package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenSecondary

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
    CardContainer(
        isChecked = isChecked,
        isError = isError,
        onClick = onSelect
    ) {
        Checkbox(
            checked = isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = brightGreenSecondary,
                checkmarkColor = Color.White
            ),
            onCheckedChange = { onCheckedChange() }
        )

        Column(
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Text(text = itemName)
            Text(text = tag)

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { onExpandedChange() }
            ) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    value = value,
                    borderColor = Color.LightGray.copy(alpha = .7f),
                    containerColor = Color.LightGray.copy(alpha = .3f),
                    hintText = SelectTitle.SelectProcessMethod.displayName,
                    isNumeric = false,
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    iconColor = Color.LightGray.copy(alpha = .8f),
                    onChange = { onValueChange(it) }
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { onDismissRequest() }
                ) {
                    DropdownMenuItem(
                        text = { Text(SelectTitle.SelectProcessMethod.displayName) },
                        onClick = { onClickDropDownMenuItem("") }
                    )
                    processTypeList.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(method.processName) },
                            onClick = {
                                onClickDropDownMenuItem(method.processName)
                            }
                        )
                    }
                }
            }
        }
    }
}

