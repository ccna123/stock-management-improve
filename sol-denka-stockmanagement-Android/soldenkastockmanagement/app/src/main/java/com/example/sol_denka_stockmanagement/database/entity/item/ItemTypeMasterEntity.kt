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
            childColumns = ["item_count_unit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemUnitMasterEntity::class,
            parentColumns = ["item_unit_id"],
            childColumns = ["item_weight_unit_id"],
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
        Index(value = ["item_count_unit_id"]),
        Index(value = ["item_weight_unit_id"]),
        Index(value = ["item_category_id"]),
    ]
)
data class ItemTypeMasterEntity(
    @PrimaryKey @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "item_count_unit_id") val itemCountUnitId: Int?,
    @ColumnInfo(name = "item_weight_unit_id") val itemWeightUnitId: Int?,
    @ColumnInfo(name = "item_category_id") val itemCategoryId: Int,
    @ColumnInfo(name = "item_type_code") val itemTypeCode: String?,
    @ColumnInfo(name = "item_type_name") val itemTypeName: String,
    @ColumnInfo(name = "packing_type") val packingType: String?,
    @ColumnInfo(name = "specific_gravity") val specificGravity: String?,
    @ColumnInfo(name = "grade") val grade: String?,
    @ColumnInfo(name = "memo") val memo: String?,
)
