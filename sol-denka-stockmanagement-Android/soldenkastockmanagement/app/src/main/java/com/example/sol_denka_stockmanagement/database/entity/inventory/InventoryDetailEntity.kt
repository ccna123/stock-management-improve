package com.example.sol_denka_stockmanagement.database.entity.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity

@Entity(
    tableName = "InventoryDetail", foreignKeys = [
        ForeignKey(
            entity = InventorySessionEntity::class,
            parentColumns = ["inventory_session_id"],
            childColumns = ["inventory_session_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = LedgerItemEntity::class,
            parentColumns = ["ledger_item_id"],
            childColumns = ["ledger_item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TagMasterEntity::class,
            parentColumns = ["tag_id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class InventoryDetailEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "inventory_detail_id") val inventoryDetailId: Int = 0,
    @ColumnInfo(name = "inventory_session_id") val inventorySessionId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "scanned_at") val scannedAt: String,
)
