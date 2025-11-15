package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.TagStatus

data class AdditionalFields(
    var tagStatus: TagStatus,
    val rssi: Float
)
