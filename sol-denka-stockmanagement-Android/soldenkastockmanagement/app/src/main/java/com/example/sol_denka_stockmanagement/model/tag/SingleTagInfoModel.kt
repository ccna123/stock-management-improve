package com.example.sol_denka_stockmanagement.model.tag

data class SingleTagInfoModel(
    val tagId: Int,
    val epc: String,
    val itemName: String,
    val itemCode: String,
    val location: String,
    val isInStock: Boolean,
    val packingType: String?,
    val specificGravity: Int?,
    val thickness: Double?,
    val length: Int?,
    val width: Int?,
    val weight: Int?,
    val lotNo: String?,
    val quantity: Int?,
    val occurrenceReason: String?,
    val occurredAt: String?,
    val processedAt: String?,
    val winderName: String?,
    val categoryName: String,
)