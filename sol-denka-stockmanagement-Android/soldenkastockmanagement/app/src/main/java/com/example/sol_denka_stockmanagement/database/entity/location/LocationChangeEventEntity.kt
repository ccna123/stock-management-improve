package com.example.sol_denka_stockmanagement.database.entity.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity

@Entity(
    tableName = "LocationChangeEvent", foreignKeys = [
        ForeignKey(
            entity = LocationChangeSessionEntity::class,
            parentColumns = ["location_change_session_id"],
            childColumns = ["location_change_session_id"],
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
            entity = LocationMasterEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["location_change_session_id"]),
        Index(value = ["ledger_item_id"]),
        Index(value = ["location_id"]),
    ]
)
data class LocationChangeEventEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_change_event_id") val locationChangeEventId: Int = 0,
    @ColumnInfo(name = "location_change_session_id") val locationChangeSessionId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "source_event_id") val sourceEventId: String,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "scanned_at") val scannedAt: String,

    )
