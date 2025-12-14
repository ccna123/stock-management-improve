package com.example.sol_denka_stockmanagement.helper.validate

import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.state.InputState

object InputValidate {
    fun validateRequiredFields(
        formItems: List<InboundInputFormModel>,
        inputState: InputState
    ): Map<String, Boolean> {
        val errors = mutableMapOf<String, Boolean>()

        formItems
            .filter { it.isRequired }
            .forEach { item ->
                when (item.fieldName) {
                    InboundInputField.OCCURRED_AT.displayName -> {
                        if (inputState.occurredAtDate.isBlank()) {
                            errors["occurred_at_date"] = true
                        }
                        if (inputState.occurredAtTime.isBlank()) {
                            errors["occurred_at_time"] = true
                        }
                    }

                    InboundInputField.PROCESSED_AT.displayName -> {
                        if (inputState.processedAtDate.isBlank()) {
                            errors["processed_at_date"] = true
                        }
                        if (inputState.processedAtTime.isBlank()) {
                            errors["processed_at_time"] = true
                        }
                    }

                    else -> {
                        val value = when (item.fieldName) {
                            InboundInputField.WEIGHT.displayName -> inputState.weight
                            InboundInputField.LENGTH.displayName -> inputState.length
                            InboundInputField.THICKNESS.displayName -> inputState.thickness
                            InboundInputField.WIDTH.displayName -> inputState.width
                            InboundInputField.SPECIFIC_GRAVITY.displayName -> inputState.specificGravity
                            InboundInputField.WINDER_INFO.displayName -> inputState.winderInfo
                            InboundInputField.OCCURRENCE_REASON.displayName -> inputState.occurrenceReason
                            InboundInputField.MEMO.displayName -> inputState.memo
                            InboundInputField.LOT_NO.displayName -> inputState.lotNo
                            InboundInputField.LOCATION.displayName -> inputState.location
                            InboundInputField.PACKING_TYPE.displayName -> inputState.packingType
                            InboundInputField.WINDER_INFO.displayName -> inputState.winderInfo
                            else -> ""
                        }
                        if (value.isBlank()) {
                            errors[item.fieldName] = true
                        }
                    }
                }
            }
        return errors
    }
}