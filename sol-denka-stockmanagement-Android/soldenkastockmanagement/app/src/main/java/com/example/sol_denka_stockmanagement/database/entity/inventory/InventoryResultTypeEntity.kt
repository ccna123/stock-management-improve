package com.example.sol_denka_stockmanagement.database.entity.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InventoryResultType")
data class InventoryResultTypeEntity(
    @PrimaryKey @ColumnInfo(name = "inventory_result_type_id") val inventoryResultTypeId: Int,
    @ColumnInfo(name = "inventory_result_code") val inventoryResultTypeCode: String,
    @ColumnInfo(name = "inventory_result_name") val inventoryResultTypeName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)