package com.example.sol_denka_stockmanagement.database.entity.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ItemCategoryMaster")
data class ItemCategoryEntity(
    @PrimaryKey @ColumnInfo(name = "item_category_id") val itemCategoryId: Int,
    @ColumnInfo(name = "item_category_name") val itemCategoryName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
