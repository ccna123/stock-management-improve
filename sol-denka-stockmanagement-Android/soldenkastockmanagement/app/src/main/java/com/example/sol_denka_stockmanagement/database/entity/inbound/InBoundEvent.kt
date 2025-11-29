package com.example.sol_denka_stockmanagement.database.entity.inbound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.leger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity

@Entity(
    tableName = "InBoundEvent",
    foreignKeys = [
        ForeignKey(
            entity = InboundSessionEntity::class,
            parentColumns = ["in_bound_session_id"],
            childColumns = ["in_bound_session_id"],
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
            entity = LocationMasterEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemTypeMasterEntity::class,
            parentColumns = ["item_type_id"],
            childColumns = ["item_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class InboundEventEntity(
    @PrimaryKey @ColumnInfo(name = "inbound_event_id") val inboundEventId: Int,
    @ColumnInfo(name = "inbound_session_id") val inboundSessionId: Int,
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int?,
    @ColumnInfo(name = "weight") val weight: Int?,
    @ColumnInfo(name = "grade") val grade: String?,
    @ColumnInfo(name = "specific_gravity") val specificGravity: String?,
    @ColumnInfo(name = "thickness") val thickness: Int?,
    @ColumnInfo(name = "width") val width: Int?,
    @ColumnInfo(name = "length") val length: Int?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "winder_info") val winderInfo: String?,
    @ColumnInfo(name = "misroll_reason") val misrollReason: String?,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "occurred_at") val occurredAt: String,
    @ColumnInfo(name = "registered_at") val registeredAt: String,
)
