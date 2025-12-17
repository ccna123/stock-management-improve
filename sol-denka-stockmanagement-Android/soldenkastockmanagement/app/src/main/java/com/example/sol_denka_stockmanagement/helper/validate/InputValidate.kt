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
                when (item.fieldCode) {
                    InboundInputField.OCCURRED_AT.code -> {
                        if (inputState.occurredAtDate.isBlank()) {
                            errors["occurred_at_date"] = true
                        }
                        if (inputState.occurredAtTime.isBlank()) {
                            errors["occurred_at_time"] = true
                        }
                    }

                    InboundInputField.PROCESSED_AT.code -> {
                        if (inputState.processedAtDate.isBlank()) {
                            errors["processed_at_date"] = true
                        }
                        if (inputState.processedAtTime.isBlank()) {
                            errors["processed_at_time"] = true
                        }
                    }

                    else -> {
                        val value = when (item.fieldCode) {
                            InboundInputField.WEIGHT.code -> inputState.weight
                            InboundInputField.LENGTH.code -> inputState.length
                            InboundInputField.THICKNESS.code -> inputState.thickness
                            InboundInputField.WIDTH.code -> inputState.width
                            InboundInputField.SPECIFIC_GRAVITY.code -> inputState.specificGravity
                            InboundInputField.WINDER.code -> inputState.winder
                            InboundInputField.OCCURRENCE_REASON.code -> inputState.occurrenceReason
                            InboundInputField.MEMO.code -> inputState.memo
                            InboundInputField.LOT_NO.code -> inputState.lotNo
                            InboundInputField.LOCATION.code -> inputState.location
                            InboundInputField.PACKING_TYPE.code -> inputState.packingType
                            InboundInputField.WINDER.code -> inputState.winder
                            InboundInputField.QUANTITY.code -> inputState.quantity
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