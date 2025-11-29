package com.example.sol_denka_stockmanagement.database.entity.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemUnitMaster")
data class ItemUnitMasterEntity(
    @PrimaryKey @ColumnInfo(name = "item_unit_id") val itemUnitId: Int,
    @ColumnInfo(name = "item_unit_code") val itemUnitCode: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
