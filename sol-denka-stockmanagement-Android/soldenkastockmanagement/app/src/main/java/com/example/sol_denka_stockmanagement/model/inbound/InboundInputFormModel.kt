package com.example.sol_denka_stockmanagement.model.inbound

import com.example.sol_denka_stockmanagement.constant.ControlType
import com.example.sol_denka_stockmanagement.constant.DataType

data class InboundInputFormModel(
    val fieldName: String,
    val controlType: ControlType,
    val dataType: DataType,
    val isRequired: Boolean,
    val isVisible: Boolean
)
