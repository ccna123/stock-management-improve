package com.example.sol_denka_stockmanagement.screen.inbound.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.PackingType
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.helper.validate.FilterNumber
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent.*
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.winder.WinderInfoModel
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundInputFormItem(
    result: InboundInputFormModel,
    locationMaster: List<LocationMasterModel>,
    winderMaster: List<WinderInfoModel>,
    expandState: ExpandState,
    inputState: InputState,
    appViewModel: AppViewModel
) {
    when (result.controlType) {

        /* ================= INPUT ================= */
        ControlType.INPUT -> {
            InputFieldContainer(
                modifier = Modifier
                    .height(if (result.fieldCode == InboundInputField.MEMO.code) 200.dp else 68.dp)
                    .fillMaxWidth(),
                value = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> inputState.weight
                    InboundInputField.LENGTH.code -> inputState.length
                    InboundInputField.THICKNESS.code -> inputState.thickness
                    InboundInputField.WIDTH.code -> inputState.width
                    InboundInputField.SPECIFIC_GRAVITY.code -> inputState.specificGravity
                    InboundInputField.OCCURRENCE_REASON.code -> inputState.occurrenceReason
                    InboundInputField.MEMO.code -> inputState.memo
                    InboundInputField.LOT_NO.code -> inputState.lotNo
                    InboundInputField.QUANTITY.code -> inputState.quantity
                    else -> ""
                },
                label = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> stringResource(
                        R.string.weight
                    )

                    InboundInputField.LENGTH.code -> stringResource(
                        R.string.length
                    )

                    InboundInputField.THICKNESS.code -> stringResource(
                        R.string.thickness
                    )

                    InboundInputField.WIDTH.code -> stringResource(
                        R.string.width
                    )

                    InboundInputField.SPECIFIC_GRAVITY.code -> stringResource(
                        R.string.specific_gravity
                    )

                    InboundInputField.WINDER.code -> stringResource(
                        R.string.winderInfo
                    )

                    InboundInputField.OCCURRENCE_REASON.code -> stringResource(
                        R.string.occurrenceReason
                    )

                    InboundInputField.MEMO.code -> stringResource(
                        R.string.memo
                    )

                    InboundInputField.LOT_NO.code -> stringResource(
                        R.string.lot_no
                    )

                    InboundInputField.QUANTITY.code -> stringResource(
                        R.string.quantity
                    )

                    else -> ""
                },
                hintText = when (result.fieldCode) {
                    InboundInputField.WEIGHT.code -> stringResource(
                        R.string.weight_hint
                    )

                    InboundInputField.LENGTH.code -> stringResource(
                        R.string.length_hint
                    )

                    InboundInputField.THICKNESS.code -> stringResource(
                        R.string.thickness_hint
                    )

                    InboundInputField.WIDTH.code -> stringResource(
                        R.string.width_hint
                    )

                    InboundInputField.SPECIFIC_GRAVITY.code -> stringResource(
                        R.string.specific_gravity_hint
                    )

                    InboundInputField.WINDER.code -> stringResource(
                        R.string.winderInfo_hint
                    )

                    InboundInputField.OCCURRENCE_REASON.code -> stringResource(
                        R.string.occurrenceReason
                    )

                    InboundInputField.MEMO.code -> stringResource(
                        R.string.memo_hint
                    )

                    InboundInputField.LOT_NO.code -> stringResource(
                        R.string.lot_no_hint
                    )

                    InboundInputField.QUANTITY.code -> stringResource(
                        R.string.quantity_hint
                    )

                    else -> ""
                },
                isNumeric = when (result.dataType) {
                    DataType.TEXT -> false
                    DataType.NUMBER -> true
                    DataType.DATETIME -> false
                },
                error = inputState.fieldErrors[result.fieldCode] == true,
                readOnly = false,
                isDropDown = false,
                enable = true,
                isRequired = result.isRequired,
                singleLine = result.fieldCode != InboundInputField.MEMO.code,
                onChange = { newValue ->
                    val filtered = if (result.dataType == DataType.NUMBER)
                        FilterNumber.filterNumber(newValue) else newValue

                    when (result.fieldCode) {
                        InboundInputField.WEIGHT.code -> appViewModel.onInputIntent(
                            ChangeWeight(
                                filtered
                            )
                        )

                        InboundInputField.LENGTH.code -> appViewModel.onInputIntent(
                            ChangeLength(
                                filtered
                            )
                        )

                        InboundInputField.THICKNESS.code -> appViewModel.onInputIntent(
                            ChangeThickness(filtered)
                        )

                        InboundInputField.WIDTH.code -> appViewModel.onInputIntent(
                            ChangeWidth(
                                filtered
                            )
                        )

                        InboundInputField.SPECIFIC_GRAVITY.code -> appViewModel.onInputIntent(
                            ChangeSpecificGravity(filtered)
                        )

                        InboundInputField.OCCURRENCE_REASON.code -> appViewModel.onInputIntent(
                            ChangeMissRollReason(filtered)
                        )

                        InboundInputField.MEMO.code -> appViewModel.onInputIntent(
                            ChangeMemo(
                                filtered
                            )
                        )

                        InboundInputField.LOT_NO.code -> appViewModel.onInputIntent(
                            ChangeLotNo(
                                filtered
                            )
                        )

                        InboundInputField.QUANTITY.code -> appViewModel.onInputIntent(
                            ChangeQuantity(
                                filtered
                            )
                        )
                    }
                }
            )
        }

        /* ================= DROPDOWN ================= */
        ControlType.DROPDOWN -> {
            ExposedDropdownMenuBox(
                modifier = Modifier.height(IntrinsicSize.Min),
                expanded = when (result.fieldCode) {
                    InboundInputField.LOCATION.code -> expandState.locationExpanded
                    InboundInputField.PACKING_TYPE.code -> expandState.packingStyleExpanded
                    InboundInputField.WINDER.code -> expandState.winderExpanded
                    else -> false
                },
                onExpandedChange = {
                    when (result.fieldCode) {
                        InboundInputField.LOCATION.code ->
                            appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded)

                        InboundInputField.PACKING_TYPE.code ->
                            appViewModel.onExpandIntent(ExpandIntent.TogglePackingTypeExpanded)

                        InboundInputField.WINDER.code ->
                            appViewModel.onExpandIntent(ExpandIntent.ToggleWinderExpanded)
                    }
                }
            ) {

                /* ---------- FIELD ---------- */
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    value = when (result.fieldCode) {
                        InboundInputField.LOCATION.code ->
                            inputState.location?.locationName ?: ""

                        InboundInputField.PACKING_TYPE.code ->
                            inputState.packingType

                        InboundInputField.WINDER.code ->
                            inputState.winder?.winderName ?: ""

                        else -> ""
                    },
                    hintText = when (result.fieldCode) {
                        InboundInputField.LOCATION.code -> SelectTitle.SelectLocation.displayName
                        InboundInputField.PACKING_TYPE.code -> SelectTitle.SelectPackingType.displayName
                        InboundInputField.WINDER.code -> SelectTitle.SelectWinder.displayName
                        else -> ""
                    },
                    label = when (result.fieldCode) {
                        InboundInputField.LOCATION.code -> SelectTitle.SelectLocation.displayName
                        InboundInputField.PACKING_TYPE.code -> SelectTitle.SelectPackingType.displayName
                        InboundInputField.WINDER.code -> SelectTitle.SelectWinder.displayName
                        else -> ""
                    },
                    isNumeric = false,
                    onChange = { /* readOnly */ },
                    isRequired = result.isRequired,
                    error = inputState.fieldErrors[result.fieldCode] == true,
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    onEnterPressed = {}
                )

                /* ---------- MENU ---------- */
                ExposedDropdownMenu(
                    expanded = when (result.fieldCode) {
                        InboundInputField.LOCATION.code -> expandState.locationExpanded
                        InboundInputField.PACKING_TYPE.code -> expandState.packingStyleExpanded
                        InboundInputField.WINDER.code -> expandState.winderExpanded
                        else -> false
                    },
                    onDismissRequest = {
                        when (result.fieldCode) {
                            InboundInputField.LOCATION.code ->
                                appViewModel.onExpandIntent(ExpandIntent.ToggleLocationExpanded)

                            InboundInputField.PACKING_TYPE.code ->
                                appViewModel.onExpandIntent(ExpandIntent.TogglePackingTypeExpanded)

                            InboundInputField.WINDER.code ->
                                appViewModel.onExpandIntent(ExpandIntent.ToggleWinderExpanded)
                        }
                    }
                ) {
                    /* ===== SELECT (NULL) ===== */
                    if (result.fieldCode == InboundInputField.LOCATION.code) {
                        DropdownMenuItem(
                            text = { Text(SelectTitle.SelectLocation.displayName) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(ChangeLocation(null))
                                    onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                }
                            }
                        )

                        locationMaster.forEach { location ->
                            DropdownMenuItem(
                                text = { Text(location.locationName) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(ChangeLocation(location))
                                        onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                    }
                                }
                            )
                        }
                    }
                    if (result.fieldCode == InboundInputField.PACKING_TYPE.code) {
                        DropdownMenuItem(
                            text = { Text(SelectTitle.SelectPackingType.displayName) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(ChangePackingType(""))
                                    onExpandIntent(ExpandIntent.TogglePackingTypeExpanded)
                                }
                            }
                        )

                        listOf(
                            PackingType.PAPER_BAG_25KG.displayName,
                            PackingType.FLEXIBLE_CONTAINER_1T.displayName
                        ).forEach { packing ->
                            DropdownMenuItem(
                                text = { Text(packing) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(ChangePackingType(packing))
                                        onExpandIntent(ExpandIntent.TogglePackingTypeExpanded)
                                    }
                                }
                            )
                        }
                    }

                    if (result.fieldCode == InboundInputField.WINDER.code) {

                        DropdownMenuItem(
                            text = { Text(SelectTitle.SelectWinder.displayName) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(ChangeWinderType(null))
                                    onExpandIntent(ExpandIntent.ToggleWinderExpanded)
                                }
                            }
                        )

                        winderMaster.forEach { winder ->
                            DropdownMenuItem(
                                text = { Text(winder.winderName) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(ChangeWinderType(winder))
                                        onExpandIntent(ExpandIntent.ToggleWinderExpanded)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        /* ================= DATETIME ================= */
        ControlType.DATETIMEPICKER -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = inputState.occurredAtDate,
                    label = stringResource(R.string.occurred_at_date),
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    error = inputState.fieldErrors["occurred_at_date"] == true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ToggleDatePicker(
                                        field = InboundInputField.OCCURRED_AT.code,
                                        showDatePicker = true
                                    )
                                )
                            }
                        )
                    }
                )
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = inputState.occurredAtTime,
                    label = stringResource(R.string.occurred_at_time),
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    error = inputState.fieldErrors["occurred_at_time"] == true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable {
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ToggleTimePicker(
                                        field = InboundInputField.OCCURRED_AT.code,
                                        showTimePicker = true
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}