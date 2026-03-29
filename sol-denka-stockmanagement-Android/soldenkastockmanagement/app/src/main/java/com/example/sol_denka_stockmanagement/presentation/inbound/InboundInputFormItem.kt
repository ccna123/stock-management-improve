package com.example.sol_denka_stockmanagement.presentation.inbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel
import com.example.sol_denka_stockmanagement.helper.validate.FilterNumber
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.primaryRed

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundInputFormItem(
    result: InboundInputFormModel,
    locationMaster: List<LocationMasterModel>,
    winderMaster: List<WinderModel>,
    uiState: InboundUiState,
    inboundViewModel: InboundViewModel
) {
    val fieldErrors = uiState.fieldErrors[result.fieldCode]
    val maxLength = when (result.fieldCode) {
        InboundInputField.MEMO.code -> 500
        InboundInputField.LOT_NO.code -> 32
        InboundInputField.OCCURRENCE_REASON.code -> 100
        else -> null
    }

    val currentLength = when (result.fieldCode) {
        InboundInputField.MEMO.code -> uiState.memo.length
        InboundInputField.LOT_NO.code -> uiState.lotNo.length
        InboundInputField.OCCURRENCE_REASON.code -> uiState.occurrenceReason.length
        else -> 0
    }

    when (result.controlType) {
        /* ================= INPUT ================= */
        ControlType.INPUT -> {
            val allowDecimal = when (result.fieldCode) {
                InboundInputField.THICKNESS.code -> true
                else -> false
            }
            InputFieldContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (result.fieldCode == InboundInputField.MEMO.code)
                            Modifier.heightIn(min = 200.dp)
                        else
                            Modifier.heightIn(min = 68.dp)
                    ),
                maxLength = maxLength,
                currentLength = currentLength,
                value = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> uiState.weight
                    InboundInputField.LENGTH.code -> uiState.length
                    InboundInputField.THICKNESS.code -> uiState.thickness
                    InboundInputField.WIDTH.code -> uiState.width
                    InboundInputField.OCCURRENCE_REASON.code -> uiState.occurrenceReason
                    InboundInputField.MEMO.code -> uiState.memo
                    InboundInputField.LOT_NO.code -> uiState.lotNo
                    InboundInputField.QUANTITY.code -> uiState.quantity
                    else -> ""
                },
                label = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> stringResource(R.string.weight)
                    InboundInputField.LENGTH.code -> stringResource(R.string.length)
                    InboundInputField.THICKNESS.code -> stringResource(R.string.thickness)
                    InboundInputField.WIDTH.code -> stringResource(R.string.width)
                    InboundInputField.OCCURRENCE_REASON.code -> stringResource(R.string.occurrenceReason)
                    InboundInputField.MEMO.code -> stringResource(R.string.memo)
                    InboundInputField.LOT_NO.code -> stringResource(R.string.lot_no)
                    InboundInputField.QUANTITY.code -> stringResource(R.string.quantity)
                    else -> ""
                },
                hintText = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> stringResource(R.string.weight_hint)
                    InboundInputField.LENGTH.code -> stringResource(R.string.length_hint)
                    InboundInputField.THICKNESS.code -> stringResource(R.string.thickness_hint)
                    InboundInputField.WIDTH.code -> stringResource(R.string.width_hint)
                    InboundInputField.WINDER.code -> stringResource(R.string.winderInfo_hint)
                    InboundInputField.OCCURRENCE_REASON.code -> stringResource(R.string.occurrenceReason)
                    InboundInputField.MEMO.code -> stringResource(R.string.memo_hint)
                    InboundInputField.LOT_NO.code -> stringResource(R.string.lot_no_hint)
                    InboundInputField.QUANTITY.code -> stringResource(R.string.quantity_hint)
                    else -> ""
                },
                isRequired = result.isRequired,
                isNumeric = result.dataType == DataType.NUMBER,
                readOnly = false,
                isDropDown = false,
                enable = true,
                singleLine = result.fieldCode != InboundInputField.MEMO.code,
                errorMessages = fieldErrors,
                onChange = { newValue ->
                    val filtered = if (result.dataType == DataType.NUMBER) {
                        FilterNumber.filterNumber(newValue, allowDecimal)
                    } else {
                        newValue.take(maxLength ?: Int.MAX_VALUE)
                    }
                    when (result.fieldCode) {
                        InboundInputField.WEIGHT.code -> inboundViewModel.onIntent(InboundIntent.WeightChanged(filtered))
                        InboundInputField.LENGTH.code -> inboundViewModel.onIntent(InboundIntent.LengthChanged(filtered))
                        InboundInputField.THICKNESS.code -> inboundViewModel.onIntent(InboundIntent.ThicknessChanged(filtered))
                        InboundInputField.WIDTH.code -> inboundViewModel.onIntent(InboundIntent.WidthChanged(filtered))
                        InboundInputField.OCCURRENCE_REASON.code -> inboundViewModel.onIntent(InboundIntent.OccurrenceReasonChanged(filtered))
                        InboundInputField.MEMO.code -> inboundViewModel.onIntent(InboundIntent.MemoChanged(filtered))
                        InboundInputField.LOT_NO.code -> inboundViewModel.onIntent(InboundIntent.LotNoChanged(filtered))
                        InboundInputField.QUANTITY.code -> inboundViewModel.onIntent(InboundIntent.QuantityChanged(filtered))
                    }
                }
            )
            if (!fieldErrors.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    fieldErrors.forEach { errorMsg ->
                        Text(text = "• $errorMsg", color = primaryRed, fontSize = 12.sp)
                    }
                }
            }
        }

        /* ================= DROPDOWN ================= */
        ControlType.DROPDOWN -> {
            val isLocation = result.fieldCode == InboundInputField.LOCATION.code
            val isWinder = result.fieldCode == InboundInputField.WINDER.code
            val expanded = if (isLocation) uiState.locationExpanded else uiState.winderExpanded

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    if (isLocation) inboundViewModel.onIntent(InboundIntent.ToggleLocationExpanded)
                    else inboundViewModel.onIntent(InboundIntent.ToggleWinderExpanded)
                }
            ) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth()
                        .heightIn(min = 68.dp),
                    value = when (result.fieldCode) {
                        InboundInputField.LOCATION.code ->
                            if (uiState.location?.locationName == SelectTitle.SelectLocation.displayName) ""
                            else uiState.location?.locationName ?: ""
                        InboundInputField.WINDER.code ->
                            if (uiState.winder?.winderName == SelectTitle.SelectWinder.displayName) ""
                            else uiState.winder?.winderName ?: ""
                        else -> ""
                    },
                    hintText = when (result.fieldCode) {
                        InboundInputField.LOCATION.code -> SelectTitle.SelectLocation.displayName
                        InboundInputField.WINDER.code -> SelectTitle.SelectWinder.displayName
                        else -> ""
                    },
                    label = when (result.fieldCode) {
                        InboundInputField.LOCATION.code -> stringResource(R.string.location)
                        InboundInputField.WINDER.code -> stringResource(R.string.winderInfo)
                        else -> ""
                    },
                    isRequired = result.isRequired,
                    isNumeric = false,
                    onChange = {},
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    errorMessages = fieldErrors,
                    onEnterPressed = {}
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        if (isLocation) inboundViewModel.onIntent(InboundIntent.ToggleLocationExpanded)
                        else inboundViewModel.onIntent(InboundIntent.ToggleWinderExpanded)
                    }
                ) {
                    if (isLocation) {
                        DropdownMenuItem(
                            text = { Text(SelectTitle.SelectLocation.displayName) },
                            onClick = {
                                inboundViewModel.onIntent(InboundIntent.LocationChanged(null))
                            }
                        )
                        locationMaster.forEach { location ->
                            DropdownMenuItem(
                                text = { Text(location.locationName) },
                                onClick = {
                                    inboundViewModel.onIntent(InboundIntent.LocationChanged(location))
                                }
                            )
                        }
                    }
                    if (isWinder) {
                        DropdownMenuItem(
                            text = { Text(SelectTitle.SelectWinder.displayName) },
                            onClick = {
                                inboundViewModel.onIntent(InboundIntent.WinderChanged(null))
                            }
                        )
                        winderMaster.forEach { winder ->
                            DropdownMenuItem(
                                text = { Text(winder.winderName) },
                                onClick = {
                                    inboundViewModel.onIntent(InboundIntent.WinderChanged(winder))
                                }
                            )
                        }
                    }
                }
            }
            if (!fieldErrors.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    fieldErrors.forEach { errorMsg ->
                        Text(text = "• $errorMsg", color = primaryRed, fontSize = 12.sp)
                    }
                }
            }
        }

        /* ================= DATETIME ================= */
        ControlType.DATETIME_PICKER -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = when (result.fieldCode) {
                        InboundInputField.OCCURRED_AT.code -> uiState.occurredAtDate
                        InboundInputField.PROCESSED_AT.code -> uiState.processedAtDate
                        else -> ""
                    },
                    label = when (result.fieldCode) {
                        InboundInputField.OCCURRED_AT.code -> stringResource(R.string.occurred_at_date)
                        InboundInputField.PROCESSED_AT.code -> stringResource(R.string.processed_at_date)
                        else -> ""
                    },
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    errorMessages = fieldErrors,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable {
                                inboundViewModel.onIntent(
                                    InboundIntent.ToggleDatePicker(
                                        field = when (result.fieldCode) {
                                            InboundInputField.OCCURRED_AT.code -> InboundInputField.OCCURRED_AT.code
                                            InboundInputField.PROCESSED_AT.code -> InboundInputField.PROCESSED_AT.code
                                            else -> ""
                                        },
                                        show = true
                                    )
                                )
                            }
                        )
                    }
                )
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = when (result.fieldCode) {
                        InboundInputField.OCCURRED_AT.code -> uiState.occurredAtTime
                        InboundInputField.PROCESSED_AT.code -> uiState.processedAtTime
                        else -> ""
                    },
                    label = when (result.fieldCode) {
                        InboundInputField.OCCURRED_AT.code -> stringResource(R.string.occurred_at_time)
                        InboundInputField.PROCESSED_AT.code -> stringResource(R.string.processed_at_time)
                        else -> ""
                    },
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    errorMessages = fieldErrors,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable {
                                inboundViewModel.onIntent(
                                    InboundIntent.ToggleTimePicker(
                                        field = when (result.fieldCode) {
                                            InboundInputField.OCCURRED_AT.code -> InboundInputField.OCCURRED_AT.code
                                            InboundInputField.PROCESSED_AT.code -> InboundInputField.PROCESSED_AT.code
                                            else -> ""
                                        },
                                        show = true
                                    )
                                )
                            }
                        )
                    }
                )
            }
            if (!fieldErrors.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    fieldErrors.forEach { errorMsg ->
                        Text(text = "• $errorMsg", color = primaryRed, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}