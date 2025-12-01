package com.example.sol_denka_stockmanagement.database.entity.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity

@Entity(
    tableName = "TagMaster",
    foreignKeys = [
        ForeignKey(
            entity = LedgerItemEntity::class,
            parentColumns = ["ledger_item_id"],
            childColumns = ["ledger_item_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["ledger_item_id"]),
        Index(value = ["epc"], unique = true)
    ],
)
data class TagMasterEntity(
    @PrimaryKey @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int?,
    @ColumnInfo(name = "epc") val epc: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
