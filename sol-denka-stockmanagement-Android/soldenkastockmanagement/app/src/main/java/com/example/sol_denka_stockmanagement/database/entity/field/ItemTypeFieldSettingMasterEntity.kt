package com.example.sol_denka_stockmanagement.database.entity.field

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ItemTypeFieldSettingMaster",
    primaryKeys = ["item_type_id", "field_id"]
)
data class ItemTypeFieldSettingMasterEntity(
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "field_id") val fieldId: Int,
    @ColumnInfo(name = "is_required") val isRequired: Boolean,
    @ColumnInfo(name = "is_visible") val isVisible: Boolean,
)
