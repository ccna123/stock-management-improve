package com.example.sol_denka_stockmanagement.database.entity.outbound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity

@Entity(
    tableName = "OutboundEvent",
    foreignKeys = [
        ForeignKey(
            entity = OutboundSessionEntity::class,
            parentColumns = ["outbound_session_id"],
            childColumns = ["outbound_session_id"],
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
            entity = ProcessTypeEntity::class,
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
    @PrimaryKey @ColumnInfo(name = "outbound_event_id") val outBoundEventId: Int,
    @ColumnInfo(name = "outbound_session_id") val outBoundSessionId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "process_type_id") val processTypeId: Int,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "occurred_at") val occurredAt: String,
    @ColumnInfo(name = "registered_at") val registeredAt: String,
)
