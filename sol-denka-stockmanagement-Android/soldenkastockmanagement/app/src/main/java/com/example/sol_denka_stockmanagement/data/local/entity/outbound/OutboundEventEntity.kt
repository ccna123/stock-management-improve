package com.example.sol_denka_stockmanagement.data.local.entity.outbound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.data.local.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity

@Entity(
    tableName = "OutboundEvent",
    foreignKeys = [
        ForeignKey(
            entity = _root_ide_package_.com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity::class,
            parentColumns = ["outbound_session_id"],
            childColumns = ["outbound_session_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = _root_ide_package_.com.example.sol_denka_stockmanagement.data.local.entity.ledger.LedgerItemEntity::class,
            parentColumns = ["ledger_item_id"],
            childColumns = ["ledger_item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = _root_ide_package_.com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity::class,
            parentColumns = ["process_type_id"],
            childColumns = ["process_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["outbound_session_id"]),
        Index(value = ["ledger_item_id"]),
        Index(value = ["process_type_id"]),
    ]
)
data class OutBoundEventEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "outbound_event_id") val outboundEventId: Int = 0,
    @ColumnInfo(name = "outbound_session_id") val outboundSessionId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "source_event_id") val sourceEventId: String,
    @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "process_type_id") val processTypeId: Int,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "processed_at") val processedAt: String?,
    @ColumnInfo(name = "registered_at") val registeredAt: String,
)
