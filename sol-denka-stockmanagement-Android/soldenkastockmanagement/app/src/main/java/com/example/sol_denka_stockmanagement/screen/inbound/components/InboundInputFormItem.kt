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
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeLength
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeLocation
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeLotNo
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeMemo
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeMissRollReason
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangePackingType
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeSpecificGravity
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeThickness
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeWeight
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeWidth
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeWinderInfo
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundInputForm(
    result: InboundInputFormModel,
    locationMaster: List<LocationMasterModel>,
    expandState: ExpandState,
    inputState: InputState,
    appViewModel: AppViewModel
) {
    when (result.controlType) {
        ControlType.INPUT -> {
            InputFieldContainer(
                modifier = Modifier
                    .height(if (result.fieldName == InboundInputField.MEMO.displayName) 200.dp else 60.dp)
                    .fillMaxWidth(),
                value = when (result.fieldName) {
                    InboundInputField.WEIGHT.displayName -> inputState.weight
                    InboundInputField.LENGTH.displayName -> inputState.length
                    InboundInputField.THICKNESS.displayName -> inputState.thickness
                    InboundInputField.WIDTH.displayName -> inputState.width
                    InboundInputField.SPECIFIC_GRAVITY.displayName -> inputState.specificGravity
                    InboundInputField.WINDER_INFO.displayName -> inputState.winderInfo
                    InboundInputField.OCCURRENCE_REASON.displayName -> inputState.occurrenceReason
                    InboundInputField.MEMO.displayName -> inputState.memo
                    InboundInputField.LOT_NO.displayName -> inputState.lotNo
                    else -> ""
                },
                label = when (result.fieldName) {
                    InboundInputField.WEIGHT.displayName -> stringResource(
                        R.string.weight
                    )

                    InboundInputField.LENGTH.displayName -> stringResource(
                        R.string.length
                    )

                    InboundInputField.THICKNESS.displayName -> stringResource(
                        R.string.thickness
                    )

                    InboundInputField.WIDTH.displayName -> stringResource(
                        R.string.width
                    )

                    InboundInputField.SPECIFIC_GRAVITY.displayName -> stringResource(
                        R.string.specific_gravity
                    )

                    InboundInputField.WINDER_INFO.displayName -> stringResource(
                        R.string.winderInfo
                    )

                    InboundInputField.OCCURRENCE_REASON.displayName -> stringResource(
                        R.string.occurrenceReason
                    )

                    InboundInputField.MEMO.displayName -> stringResource(
                        R.string.memo
                    )

                    InboundInputField.LOT_NO.displayName -> stringResource(
                        R.string.lot_no
                    )

                    else -> ""
                },
                hintText = when (result.fieldName) {
                    InboundInputField.WEIGHT.displayName -> stringResource(
                        R.string.weight_hint
                    )

                    InboundInputField.LENGTH.displayName -> stringResource(
                        R.string.length_hint
                    )

                    InboundInputField.THICKNESS.displayName -> stringResource(
                        R.string.thickness_hint
                    )

                    InboundInputField.WIDTH.displayName -> stringResource(
                        R.string.width_hint
                    )

                    InboundInputField.SPECIFIC_GRAVITY.displayName -> stringResource(
                        R.string.specific_gravity_hint
                    )

                    InboundInputField.WINDER_INFO.displayName -> stringResource(
                        R.string.winderInfo_hint
                    )

                    InboundInputField.OCCURRENCE_REASON.displayName -> stringResource(
                        R.string.occurrenceReason
                    )

                    InboundInputField.MEMO.displayName -> stringResource(
                        R.string.memo_hint
                    )

                    InboundInputField.LOT_NO.displayName -> stringResource(
                        R.string.lot_no_hint
                    )

                    else -> ""
                },
                isNumeric = when (result.dataType) {
                    DataType.TEXT -> false
                    DataType.NUMBER -> true
                    DataType.DATETIME -> false
                },
                error = inputState.fieldErrors[result.fieldName] == true,
                readOnly = false,
                isDropDown = false,
                enable = true,
                isRequired = result.isRequired,
                singleLine = result.fieldName != InboundInputField.MEMO.displayName,
                onChange = { newValue ->
                    val filteredValue = when (result.dataType) {
                        DataType.NUMBER -> FilterNumber.filterNumber(newValue)
                        else -> newValue
                    }
                    when (result.fieldName) {
                        InboundInputField.WEIGHT.displayName -> appViewModel.onInputIntent(
                            ChangeWeight(filteredValue)
                        )

                        InboundInputField.LENGTH.displayName -> appViewModel.onInputIntent(
                            ChangeLength(filteredValue)
                        )

                        InboundInputField.THICKNESS.displayName -> appViewModel.onInputIntent(
                            ChangeThickness(filteredValue)
                        )

                        InboundInputField.WIDTH.displayName -> appViewModel.onInputIntent(
                            ChangeWidth(filteredValue)
                        )

                        InboundInputField.SPECIFIC_GRAVITY.displayName -> appViewModel.onInputIntent(
                            ChangeSpecificGravity(filteredValue)
                        )

                        InboundInputField.WINDER_INFO.displayName -> appViewModel.onInputIntent(
                            ChangeWinderInfo(newValue)
                        )

                        InboundInputField.OCCURRENCE_REASON.displayName -> appViewModel.onInputIntent(
                            ChangeMissRollReason(newValue)
                        )

                        InboundInputField.MEMO.displayName -> appViewModel.onInputIntent(
                            ChangeMemo(newValue)
                        )

                        InboundInputField.LOT_NO.displayName -> appViewModel.onInputIntent(
                            ChangeLotNo(newValue)
                        )
                    }
                }
            )
        }

        ControlType.DROPDOWN -> {
            ExposedDropdownMenuBox(
                modifier = Modifier.height(IntrinsicSize.Min),
                expanded = when (result.fieldName) {
                    InboundInputField.LOCATION.displayName -> expandState.locationExpanded
                    InboundInputField.PACKING_TYPE.displayName -> expandState.packingStyleExpanded
                    else -> false
                },
                onExpandedChange = {
                    when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> {
                            appViewModel.onExpandIntent(
                                ExpandIntent.ToggleLocationExpanded
                            )
                        }

                        InboundInputField.PACKING_TYPE.displayName -> {
                            appViewModel.onExpandIntent(
                                ExpandIntent.TogglePackingTypeExpanded
                            )
                        }

                        else -> false
                    }

                }
            ) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    value = when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> if (inputState.location == SelectTitle.SelectLocation.displayName) "" else inputState.location
                        InboundInputField.PACKING_TYPE.displayName -> if (inputState.packingType == SelectTitle.SelectPackingStyle.displayName) "" else inputState.packingType
                        else -> ""
                    },
                    hintText = when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> SelectTitle.SelectLocation.displayName
                        InboundInputField.PACKING_TYPE.displayName -> SelectTitle.SelectPackingStyle.displayName
                        else -> ""
                    },
                    label = when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> SelectTitle.SelectLocation.displayName
                        InboundInputField.PACKING_TYPE.displayName -> SelectTitle.SelectPackingStyle.displayName
                        else -> ""
                    },
                    isNumeric = false,
                    onChange = { newValue ->
                        when (result.fieldName) {
                            InboundInputField.LOCATION.displayName -> appViewModel.onInputIntent(
                                ChangeLocation(newValue)
                            )

                            InboundInputField.PACKING_TYPE.displayName -> appViewModel.onInputIntent(
                                ChangePackingType(
                                    newValue
                                )
                            )

                            else -> ""
                        }
                    },
                    isRequired = result.isRequired,
                    error = inputState.fieldErrors[result.fieldName] == true,
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    onEnterPressed = {}
                )
                ExposedDropdownMenu(
                    expanded = when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> expandState.locationExpanded
                        InboundInputField.PACKING_TYPE.displayName -> expandState.packingStyleExpanded
                        else -> false
                    },
                    onDismissRequest = {
                        when (result.fieldName) {
                            InboundInputField.LOCATION.displayName -> appViewModel.onExpandIntent(
                                ExpandIntent.ToggleLocationExpanded
                            )

                            InboundInputField.PACKING_TYPE.displayName -> appViewModel.onExpandIntent(
                                ExpandIntent.TogglePackingTypeExpanded
                            )

                            else -> ""
                        }
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = when (result.fieldName) {
                                    InboundInputField.LOCATION.displayName -> SelectTitle.SelectLocation.displayName
                                    InboundInputField.PACKING_TYPE.displayName -> SelectTitle.SelectPackingStyle.displayName
                                    else -> ""
                                }
                            )
                        },
                        onClick = {
                            when (result.fieldName) {
                                InboundInputField.LOCATION.displayName -> {
                                    appViewModel.apply {
                                        onInputIntent(
                                            ChangeLocation(
                                                ""
                                            )
                                        )
                                        onExpandIntent(ExpandIntent.ToggleLocationExpanded)
                                    }
                                }

                                InboundInputField.PACKING_TYPE.displayName -> {
                                    appViewModel.apply {
                                        onInputIntent(
                                            ChangePackingType(
                                                ""
                                            )
                                        )
                                        onExpandIntent(ExpandIntent.TogglePackingTypeExpanded)
                                    }
                                }

                                else -> ""
                            }
                        }
                    )
                    when (result.fieldName) {
                        InboundInputField.LOCATION.displayName -> {
                            locationMaster.forEach { location ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = location.locationName
                                        )
                                    },
                                    onClick = {
                                        appViewModel.apply {
                                            onInputIntent(
                                                ChangeLocation(
                                                    if (location.locationName == SelectTitle.SelectLocation.displayName) "" else location.locationName
                                                )
                                            )
                                            onExpandIntent(
                                                ExpandIntent.ToggleLocationExpanded
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        InboundInputField.PACKING_TYPE.displayName -> {
                            listOf(
                                PackingType.PAPER_BAG_25KG.displayName,
                                PackingType.FLEXIBLE_CONTAINER_1T.displayName
                            ).forEach { packingStyle ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = packingStyle)
                                    },
                                    onClick = {
                                        appViewModel.apply {
                                            onInputIntent(
                                                ChangePackingType(
                                                    if (inputState.packingType == SelectTitle.SelectPackingStyle.displayName) "" else packingStyle
                                                )
                                            )
                                            onExpandIntent(
                                                ExpandIntent.TogglePackingTypeExpanded
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        ControlType.DATETIMEPICKER -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> inputState.occurredAtDate
                        InboundInputField.PROCESSED_AT.displayName -> inputState.processedAtDate
                        else -> ""
                    },
                    label = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> stringResource(
                            R.string.occurred_at_date
                        )

                        InboundInputField.PROCESSED_AT.displayName -> stringResource(
                            R.string.processed_at_date
                        )

                        else -> ""
                    },
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    error = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> inputState.fieldErrors["occurred_at_date"] == true
                        InboundInputField.PROCESSED_AT.displayName -> inputState.fieldErrors["processed_at_date"] == true
                        else -> false
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable(
                                onClick = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleDatePicker(
                                            field = when (result.fieldName) {
                                                InboundInputField.OCCURRED_AT.displayName -> InboundInputField.OCCURRED_AT.displayName
                                                InboundInputField.PROCESSED_AT.displayName -> InboundInputField.PROCESSED_AT.displayName
                                                else -> ""
                                            },
                                            showDatePicker = true
                                        )
                                    )
                                }
                            )
                        )
                    }
                )
                InputFieldContainer(
                    modifier = Modifier.weight(1f),
                    value = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> inputState.occurredAtTime
                        InboundInputField.PROCESSED_AT.displayName -> inputState.processedAtTime
                        else -> ""
                    },
                    label = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> stringResource(
                            R.string.occurred_at_time
                        )

                        InboundInputField.PROCESSED_AT.displayName -> stringResource(
                            R.string.processed_at_time
                        )

                        else -> ""
                    },
                    isRequired = result.isRequired,
                    isNumeric = false,
                    shape = RoundedCornerShape(13.dp),
                    readOnly = true,
                    isDropDown = false,
                    enable = true,
                    error = when (result.fieldName) {
                        InboundInputField.OCCURRED_AT.displayName -> inputState.fieldErrors["occurred_at_time"] == true
                        InboundInputField.PROCESSED_AT.displayName -> inputState.fieldErrors["processed_at_time"] == true
                        else -> false
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = brightAzure,
                            modifier = Modifier.clickable(
                                onClick = {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ToggleTimePicker(
                                            field = when (result.fieldName) {
                                                InboundInputField.OCCURRED_AT.displayName -> InboundInputField.OCCURRED_AT.displayName
                                                InboundInputField.PROCESSED_AT.displayName -> InboundInputField.PROCESSED_AT.displayName
                                                else -> ""
                                            },
                                            showTimePicker = true
                                        )
                                    )
                                }
                            )
                        )
                    }
                )
            }
        }
    }
}