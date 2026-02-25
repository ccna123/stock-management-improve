package com.example.sol_denka_stockmanagement.helper.validate

import android.util.Log
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.state.InputState

object InputValidate {

    fun validate(
        formItems: List<InboundInputFormModel>,
        inputState: InputState
    ): Map<String, List<String>> {

        val errors = mutableMapOf<String, MutableList<String>>()

        fun addError(field: String, message: String) {
            val list = errors.getOrPut(field) { mutableListOf() }
            if (!list.contains(message)) {
                list.add(message)
            }
        }

        /* ================= REQUIRED ================= */

        formItems.filter { it.isRequired }.filter { it.isVisible }.forEach { item ->
            when (item.fieldCode) {

                InboundInputField.LOCATION.code -> {
                    if (inputState.location == null)
                        addError(item.fieldCode, "必須項目です")
                }

                InboundInputField.WINDER.code -> {
                    if (inputState.winder == null)
                        addError(item.fieldCode, "必須項目です")
                }

                InboundInputField.OCCURRED_AT.code -> {
                    if (inputState.occurredAtDate.isBlank() ||
                        inputState.occurredAtTime.isBlank()
                    ) {
                        addError(item.fieldCode, "必須項目です")
                    }
                }

                InboundInputField.PROCESSED_AT.code -> {
                    if (inputState.processedAtDate.isBlank() ||
                        inputState.processedAtTime.isBlank()
                    ) {
                        addError(item.fieldCode, "必須項目です")
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

                    if (value.isBlank())
                        addError(item.fieldCode, "必須項目です")
                }
            }
        }
        Log.e("TSS", "formItems: $formItems", )
        Log.e("TSS", "errors: $errors", )

        /* ================= RANGE ================= */

        inputState.width.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.WIDTH.code, "0～9999の範囲で入力してください")
        }

        inputState.length.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.LENGTH.code, "0～9999の範囲で入力してください")
        }

        inputState.weight.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.WEIGHT.code, "0～9999の範囲で入力してください")
        }

        inputState.quantity.toIntOrNull()?.let {
            if (it !in 0..999999)
                addError(InboundInputField.QUANTITY.code, "0～999999の範囲で入力してください")
        }

        inputState.thickness.toDoubleOrNull()?.let {
            if (it < 0.0 || it > 999.999)
                addError(InboundInputField.THICKNESS.code, "0.000～999.999の範囲で入力してください")
        }

        /* ================= DATETIME PAIR ================= */

        if (inputState.occurredAtDate.isNotBlank() xor
            inputState.occurredAtTime.isNotBlank()
        ) {
            addError(
                InboundInputField.OCCURRED_AT.code,
                "日付と時間は両方入力してください"
            )
        }

        if (inputState.processedAtDate.isNotBlank() xor
            inputState.processedAtTime.isNotBlank()
        ) {
            addError(
                InboundInputField.PROCESSED_AT.code,
                "日付と時間は両方入力してください"
            )
        }

        return errors
    }
}