package com.example.sol_denka_stockmanagement.model.outbound

data class OutboundScanDataTable(
    val epc: String,
    val itemName: String?,
    val processType: String
)

data class OutboundScanResultDTO(
    val epc: String,
    val itemName: String?,
)

