package com.example.sol_denka_stockmanagement.domain.model.inbound

data class InboundScanResult(
    val epc: String,
    val itemName: String?,
    val itemCode: String?,
    val timeStamp: String?
)
