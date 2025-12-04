package com.example.sol_denka_stockmanagement.database.entity.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity

@Entity(
    tableName = "InventoryResultLocal", foreignKeys = [
        ForeignKey(
            entity = InventorySessionEntity::class,
            parentColumns = ["inventory_session_id"],
            childColumns = ["inventory_session_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InventoryResultTypeEntity::class,
            parentColumns = ["inventory_result_type_id"],
            childColumns = ["inventory_result_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE

        ),
        ForeignKey(
            entity = LedgerItemEntity::class,
            parentColumns = ["ledger_item_id"],
            childColumns = ["ledger_item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE

        ),
        ForeignKey(
            entity = TagMasterEntity::class,
            parentColumns = ["tag_id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE

        )
    ],
    indices = [
        Index(value = ["inventory_session_id"]),
        Index(value = ["inventory_result_type_id"]),
        Index(value = ["ledger_item_id"]),
        Index(value = ["tag_id"])
    ]
)
data class InventoryResultLocalEntity(
    @PrimaryKey @ColumnInfo(name = "inventory_result_id") val inventoryResultId: Int,
    @ColumnInfo(name = "inventory_session_id") val inventorySessionId: Int,
    @ColumnInfo(name = "inventory_result_type_id") val inventoryResultTypeId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "scanned_at") val scannedAt: String?,
)
