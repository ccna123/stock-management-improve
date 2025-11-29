package com.example.sol_denka_stockmanagement.database.entity.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.constant.InventoryResultCode

@Entity(tableName = "InventoryResultType")
data class InventoryResultTypeEntity(
    @PrimaryKey @ColumnInfo(name = "inventory_result_type_id") val inventoryResultTypeId: Int,
    @PrimaryKey @ColumnInfo(name = "inventory_result_code") val inventoryResultCode: InventoryResultCode,
    @PrimaryKey @ColumnInfo(name = "inventory_result_name") val inventoryResultName: String,
    @PrimaryKey @ColumnInfo(name = "created_at") val createdAt: String,
    @PrimaryKey @ColumnInfo(name = "updated_at") val updatedAt: String,

    )
