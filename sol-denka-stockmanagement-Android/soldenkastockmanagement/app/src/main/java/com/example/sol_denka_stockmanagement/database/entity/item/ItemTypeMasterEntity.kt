package com.example.sol_denka_stockmanagement.database.entity.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ItemTypeMaster", foreignKeys = [
        ForeignKey(
            entity = ItemUnitMasterEntity::class,
            parentColumns = ["item_unit_id"],
            childColumns = ["item_unit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemCategoryEntity::class,
            parentColumns = ["item_category_id"],
            childColumns = ["item_category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["item_unit_id"]),
        Index(value = ["item_category_id"]),
    ]
)
data class ItemTypeMasterEntity(
    @PrimaryKey @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "item_unit_id") val itemUnitId: Int,
    @ColumnInfo(name = "item_category_id") val itemCategoryId: Int,
    @ColumnInfo(name = "item_type_code") val itemTypeCode: String,
    @ColumnInfo(name = "item_type_name") val itemTypeName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
