package com.example.sol_denka_stockmanagement.database.entity.ledger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity

@Entity(
    tableName = "LedgerItem", foreignKeys = [
        ForeignKey(
            entity = ItemTypeMasterEntity::class,
            parentColumns = ["item_type_id"],
            childColumns = ["item_type_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WinderInfoEntity::class,
            parentColumns = ["winder_id"],
            childColumns = ["winder_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["item_type_id"]),
        Index(value = ["location_id"]),
        Index(value = ["winder_id"]),
    ]
)
data class LedgerItemEntity(
    @PrimaryKey @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "winder_id") val winderInfoId: Int?,
    @ColumnInfo(name = "is_in_stock") val isInStock: Boolean,
    @ColumnInfo(name = "weight") val weight: Int?,
    @ColumnInfo(name = "width") val width: Int?,
    @ColumnInfo(name = "length") val length: Int?,
    @ColumnInfo(name = "thickness") val thickness: Int?,
    @ColumnInfo(name = "lot_no") val lotNo: Int?,
    @ColumnInfo(name = "occurrence_reason") val occurrenceReason: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "occurred_at") val occurredAt: String?,
    @ColumnInfo(name = "processed_at") val processedAt: String?,
)
