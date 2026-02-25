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

        /* ================= REQUIRED CHECK ================= */

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

                    InboundInputField.LOCATION.code -> {
                        if (inputState.location == null) {
                            errors[item.fieldCode] = true
                        }
                    }

                    InboundInputField.WINDER.code -> {
                        if (inputState.winder == null) {
                            errors[item.fieldCode] = true
                        }
                    }

                    else -> {
                        val value = when (item.fieldCode) {
                            InboundInputField.WEIGHT.code -> inputState.weight
                            InboundInputField.LENGTH.code -> inputState.length
                            InboundInputField.THICKNESS.code -> inputState.thickness
                            InboundInputField.WIDTH.code -> inputState.width
                            InboundInputField.OCCURRENCE_REASON.code -> inputState.occurrenceReason
                            InboundInputField.MEMO.code -> inputState.memo
                            InboundInputField.LOT_NO.code -> inputState.lotNo
                            InboundInputField.QUANTITY.code -> inputState.quantity
                            else -> ""
                        }

                        if (value.isBlank()) {
                            errors[item.fieldCode] = true
                        }
                    }
                }
            }

        /* ================= RANGE CHECK ================= */

        // 巾 0～9999
        inputState.width.toIntOrNull()?.let {
            if (it !in 0..9999) {
                errors[InboundInputField.WIDTH.code] = true
            }
        }

        // 長さ 0～9999
        inputState.length.toIntOrNull()?.let {
            if (it !in 0..9999) {
                errors[InboundInputField.LENGTH.code] = true
            }
        }

        // 重量 0～9999
        inputState.weight.toIntOrNull()?.let {
            if (it !in 0..9999) {
                errors[InboundInputField.WEIGHT.code] = true
            }
        }

        // 数量 0～999999
        inputState.quantity.toIntOrNull()?.let {
            if (it !in 0..999999) {
                errors[InboundInputField.QUANTITY.code] = true
            }
        }

        // 厚さ 0.000～999.999
        inputState.thickness.toDoubleOrNull()?.let {
            if (it < 0.0 || it > 999.999) {
                errors[InboundInputField.THICKNESS.code] = true
            }
        }

        // 厚さ 小数点3桁制限
        if (inputState.thickness.contains(".")) {
            val decimal = inputState.thickness.substringAfter(".")
            if (decimal.length > 3) {
                errors[InboundInputField.THICKNESS.code] = true
            }
        }

        /* ================= DATETIME PAIR CHECK ================= */
        // 日付のみ／時間のみ入力は不可

        val occurredDate = inputState.occurredAtDate
        val occurredTime = inputState.occurredAtTime

        if ((occurredDate.isNotBlank() && occurredTime.isBlank()) ||
            (occurredDate.isBlank() && occurredTime.isNotBlank())
        ) {
            errors[InboundInputField.OCCURRED_AT.code] = true
        }

        val processedDate = inputState.processedAtDate
        val processedTime = inputState.processedAtTime

        if ((processedDate.isNotBlank() && processedTime.isBlank()) ||
            (processedDate.isBlank() && processedTime.isNotBlank())
        ) {
            errors[InboundInputField.PROCESSED_AT.code] = true
        }

        return errors
    }
}