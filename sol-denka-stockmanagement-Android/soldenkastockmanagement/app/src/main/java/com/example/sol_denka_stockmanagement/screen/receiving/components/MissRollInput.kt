package com.example.sol_denka_stockmanagement.screen.receiving.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.PackingStyleItem
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.share.InputFieldContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissRollInput(
    thickness: String,
    rollingMachineInfo: String,
    stockArea: String,
    length: String,
    packingStyle: String,
    packingStyleExpanded: Boolean,
    onThicknessChange: (String) -> Unit,
    onLengthChange: (String) -> Unit,
    onRollingMachineInfoChange: (String) -> Unit,
    onStockAreaChange: (String) -> Unit,
    onPackingStyleChange: (String) -> Unit,
    onPackingStyleExpand: (Boolean) -> Unit,
) {
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = thickness,
        label = stringResource(R.string.thickness),
        hintText = stringResource(R.string.thickness_hint),
        isNumeric = false,
        readOnly = false,
        isDropDown = false,
        enable = true,
        onChange = { newValue ->
            val filteredValue = newValue.filter { char ->
                (char.isLetterOrDigit() && char.toString()
                    .toByteArray().size == 1) || char == '-'
            }
            onThicknessChange(filteredValue)
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = length,
        label = stringResource(R.string.length),
        hintText = stringResource(R.string.length_hint),
        isNumeric = true,
        readOnly = false,
        isDropDown = false,
        enable = true,
        onChange = { newValue ->
            val filteredValue = newValue.filter { char -> char.isDigit() || char == '.' }
            onLengthChange(filteredValue)
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = rollingMachineInfo,
        label = stringResource(R.string.rolling_machine),
        hintText = stringResource(R.string.rolling_machine_hint),
        isNumeric = false,
        readOnly = false,
        isDropDown = false,
        enable = true,
        onChange = { newValue ->
            val filteredValue = newValue.filter { char ->
                (char.isLetterOrDigit() && char.toString()
                    .toByteArray().size == 1) || char == '-'
            }
            onRollingMachineInfoChange(filteredValue)
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = stockArea,
        label = stringResource(R.string.storage_area),
        hintText = stringResource(R.string.storage_area_hint),
        isNumeric = false,
        shape = RoundedCornerShape(13.dp),
        readOnly = false,
        isDropDown = false,
        enable = true,
        onChange = { newValue ->
            val filteredValue = newValue.filter { char ->
                (char.isLetterOrDigit() && char.toString()
                    .toByteArray().size == 1) || char == '-'
            }
            onStockAreaChange(filteredValue)
        }
    )
    Spacer(modifier = Modifier.height(18.dp))
    ExposedDropdownMenuBox(
        expanded = packingStyleExpanded,
        onExpandedChange = { onPackingStyleExpand(it) }) {
        InputFieldContainer(
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            value = if (packingStyle == SelectTitle.SelectPackingStyle.displayName) "" else packingStyle,
            isNumeric = false,
            hintText = SelectTitle.SelectPackingStyle.displayName,
            onChange = { newValue ->
                onPackingStyleChange(newValue)
            },
            readOnly = true,
            isDropDown = true,
            enable = true,
        )
        ExposedDropdownMenu(
            expanded = packingStyleExpanded,
            onDismissRequest = { onPackingStyleExpand(false) }
        ) {
            listOf(
                SelectTitle.SelectPackingStyle.displayName,
                PackingStyleItem.FLEXIBLE_CONTAINER_1T.displayName,
                PackingStyleItem.PAPER_BAG_25KG.displayName,
            ).forEach { packingStyle ->
                DropdownMenuItem(
                    text = { Text(text = packingStyle) },
                    onClick = {
                        onPackingStyleChange(packingStyle)
                        onPackingStyleExpand(false)
                    }
                )
            }
        }
    }
}