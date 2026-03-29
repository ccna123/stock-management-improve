package com.example.sol_denka_stockmanagement.helper.validate

import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.presentation.inbound.InboundUiState

object InputValidate {

    fun validate(
        formItems: List<InboundInputFormModel>,
        uiState: InboundUiState
    ): Map<String, List<String>> {

        val errors = mutableMapOf<String, MutableList<String>>()

        fun addError(field: String, message: String) {
            val list = errors.getOrPut(field) { mutableListOf() }
            if (!list.contains(message)) {
                list.add(message)
            }
        }

        /* ================= REQUIRED ================= */

        formItems.filter { it.isRequired }.forEach { item ->
            when (item.fieldCode) {

                InboundInputField.LOCATION.code -> {
                    if (uiState.location == null)
                        addError(item.fieldCode, "必須項目です")
                }

                InboundInputField.WINDER.code -> {
                    if (uiState.winder == null)
                        addError(item.fieldCode, "必須項目です")
                }

                InboundInputField.OCCURRED_AT.code -> {
                    if (uiState.occurredAtDate.isBlank() ||
                        uiState.occurredAtTime.isBlank()
                    ) {
                        addError(item.fieldCode, "必須項目です")
                    }
                }

                InboundInputField.PROCESSED_AT.code -> {
                    if (uiState.processedAtDate.isBlank() ||
                        uiState.processedAtTime.isBlank()
                    ) {
                        addError(item.fieldCode, "必須項目です")
                    }
                }


                else -> {
                    val value = when (item.fieldCode) {
                        InboundInputField.WEIGHT.code -> uiState.weight
                        InboundInputField.LENGTH.code -> uiState.length
                        InboundInputField.THICKNESS.code -> uiState.thickness
                        InboundInputField.WIDTH.code -> uiState.width
                        InboundInputField.OCCURRENCE_REASON.code -> uiState.occurrenceReason
                        InboundInputField.MEMO.code -> uiState.memo
                        InboundInputField.LOT_NO.code -> uiState.lotNo
                        InboundInputField.QUANTITY.code -> uiState.quantity
                        else -> ""
                    }

                    if (value.isBlank())
                        addError(item.fieldCode, "必須項目です")
                }
            }
        }

        /* ================= RANGE ================= */

        uiState.width.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.WIDTH.code, "0～9999の範囲で入力してください")
        }

        uiState.length.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.LENGTH.code, "0～9999の範囲で入力してください")
        }

        uiState.weight.toIntOrNull()?.let {
            if (it !in 0..9999)
                addError(InboundInputField.WEIGHT.code, "0～9999の範囲で入力してください")
        }

        uiState.quantity.toIntOrNull()?.let {
            if (it !in 0..999999)
                addError(InboundInputField.QUANTITY.code, "0～999999の範囲で入力してください")
        }

        uiState.thickness.toDoubleOrNull()?.let {
            if (it !in 0.0..999.999)
                addError(InboundInputField.THICKNESS.code, "0.000～999.999の範囲で入力してください")
        }

        /* ================= DATETIME PAIR ================= */

        if (uiState.occurredAtDate.isNotBlank() xor
            uiState.occurredAtTime.isNotBlank()
        ) {
            addError(
                InboundInputField.OCCURRED_AT.code,
                "日付と時間は両方入力してください"
            )
        }

        if (uiState.processedAtDate.isNotBlank() xor
            uiState.processedAtTime.isNotBlank()
        ) {
            addError(
                InboundInputField.PROCESSED_AT.code,
                "日付と時間は両方入力してください"
            )
        }

        return errors
    }
}