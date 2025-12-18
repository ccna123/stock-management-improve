package com.example.sol_denka_stockmanagement.model.tag

data class SingleTagInfoModel(
    val tagId: Int,
    val epc: String,
    val itemName: String,
    val itemCode: String,
    val location: String,
    val isInStock: Boolean
)