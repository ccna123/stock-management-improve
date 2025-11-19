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
import com.example.sol_denka_stockmanagement.share.InputFieldContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiterInput(
    thickness: String,
    stockArea: String,
    lotNo: String,
    packingStyle: String,
    packingStyleExpanded: Boolean,
    onThicknessChange: (String) -> Unit,
    onStockAreaChange: (String) -> Unit,
    onLotNoChange: (String) -> Unit,
    onPackingStyleChange: (String) -> Unit,
    onPackingStyleExpand: (Boolean) -> Unit,
) {
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = thickness,
        label = stringResource(R.string.weight),
        hintText = stringResource(R.string.weight_hint),
        isNumeric = true,
        shape = RoundedCornerShape(13.dp),
        readOnly = false,
        isDropDown = false,
        enable = true,
        onChange = { newValue ->
            val filteredValue = newValue.filter { char -> char.isDigit() || char == '.' }
            onThicknessChange(filteredValue)
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
    Spacer(modifier = Modifier.height(10.dp))
    InputFieldContainer(
        modifier = Modifier.fillMaxWidth(),
        value = lotNo,
        label = stringResource(R.string.lot_no),
        hintText = stringResource(R.string.lot_no_hint),
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
            onLotNoChange(filteredValue)
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
            value = if (packingStyle == PackingStyleItem.SELECTION_TITLE.displayName) "" else packingStyle,
            isNumeric = false,
            hintText = PackingStyleItem.SELECTION_TITLE.displayName,
            shape = RoundedCornerShape(13.dp),
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
                PackingStyleItem.SELECTION_TITLE.displayName,
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