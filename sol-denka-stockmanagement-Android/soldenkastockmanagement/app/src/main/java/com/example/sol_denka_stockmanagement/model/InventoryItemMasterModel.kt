package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.entity.InventoryItemMasterEntity

data class InventoryItemMasterModel(
    val id: Int,
    val materialId: Int,
    val locationId: Int,
    val itemName: String,
    val epc: String,
    val isPresent: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val newField: AdditionalFields
)

fun InventoryItemMasterModel.toInventoryItemMasterEntity(): InventoryItemMasterEntity {
    return InventoryItemMasterEntity(
        id = id,
        materialId = materialId,
        locationId = locationId,
        itemName = itemName,
        epc = epc,
        isPresent = isPresent,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun InventoryItemMasterEntity.toInventoryItemMasterModel(): InventoryItemMasterModel{
    return InventoryItemMasterModel(
        id = id,
        materialId = materialId,
        locationId = locationId,
        itemName = itemName,
        epc = epc,
        isPresent = isPresent,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        newField = AdditionalFields(
            tagStatus = TagStatus.UNPROCESSED,
            rssi = -100f
        )
    )
}
