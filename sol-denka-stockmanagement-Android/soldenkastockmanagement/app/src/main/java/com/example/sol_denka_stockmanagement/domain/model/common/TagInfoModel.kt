package com.example.sol_denka_stockmanagement.domain.model.common

data class TagInfoModel(
    val rfidNo: String,
    val rssi: Float = 0f,
)
