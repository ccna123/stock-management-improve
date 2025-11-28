package com.example.sol_denka_stockmanagement.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.constant.CsvHistoryTarget
import com.example.sol_denka_stockmanagement.constant.EventTypeCode
import com.example.sol_denka_stockmanagement.constant.InventoryResultType

@Entity(tableName = "LocationMaster")
data class LocationMasterEntity(
    @PrimaryKey @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "location_code") val locationCode: String,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(
    tableName = "LedgerItem", foreignKeys = [
        ForeignKey(
            entity = ItemTypeMasterEntity::class,
            parentColumns = ["item_type_id"],
            childColumns = ["item_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LedgerItemEntity(
    @PrimaryKey @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "is_in_stock") val isInStock: Boolean,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "grade") val grade: String,
    @ColumnInfo(name = "specific_gravity") val specificGravity: Int,
    @ColumnInfo(name = "thickness") val thickness: Int,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "length") val length: Int,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "winder_info") val winderInfo: String,
    @ColumnInfo(name = "misroll_reason") val misrollReason: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(tableName = "LocationChangeSession")
data class LocationChangeSessionEntity(
    @PrimaryKey @ColumnInfo(name = "location_change_session_id") val locationChangeSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)

@Entity(tableName = "MaterialMaster", [Index(value = ["material_code"], unique = true)])
data class MaterialMasterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "material_code") val materialCode: String,
    @ColumnInfo(name = "material_name") val materialName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(tableName = "CsvHistory")
data class CsvHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "direction") val direction: CsvHistoryDirection,
    @ColumnInfo(name = "target") val target: CsvHistoryTarget,
    @ColumnInfo(name = "result") val result: CsvHistoryResult,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)

@Entity(tableName = "EventTypeMaster")
data class EventTypeMasterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "event_code") val eventTypeCode: EventTypeCode,
    @ColumnInfo(name = "event_name") val eventTypeName: String,
)

@Entity(
    tableName = "InventoryItemMaster", foreignKeys = [
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MaterialMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["material_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class InventoryItemMasterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "material_id") val materialId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "epc") val epc: String,
    @ColumnInfo(name = "is_present") val isPresent: Int,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(
    tableName = "InventoryTask", foreignKeys = [
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class InventoryTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "is_export_csv") val isExportCsv: Int,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)

@Entity(
    tableName = "InventoryResult", foreignKeys = [
        ForeignKey(
            entity = InventoryTaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InventoryItemMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE

        )
    ]
)
data class InventoryResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "task_id") val taskId: Int,
    @ColumnInfo(name = "item_id") val itemId: Int,
    @ColumnInfo(name = "epc") val epc: String,
    @ColumnInfo(name = "scan_result_type") val scanResultType: InventoryResultType,
    @ColumnInfo(name = "scanned_at") val scannedAt: String,
)

@Entity(
    tableName = "InOutEvent", foreignKeys = [
        ForeignKey(
            entity = MaterialMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["material_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EventTypeMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["from_location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["to_location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["performed_location_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class InOutEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "item_id") val itemId: Int? = null,
    @ColumnInfo(name = "material_id") val materialId: Int,
    @ColumnInfo(name = "event_type_id") val eventTypeId: Int,
    @ColumnInfo(name = "performed_location_id") val performedLocationId: Int,
    @ColumnInfo(name = "from_location_id") val fromLocationId: Int,
    @ColumnInfo(name = "to_location_id") val toLocationId: Int,
    @ColumnInfo(name = "epc") val epc: String,
    @ColumnInfo(name = "is_present") val isPresent: Int,
    @ColumnInfo(name = "is_export_csv") val isExportCsv: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "occurred_at") val occurredAt: String,
)

@Entity(tableName = "ItemUnitMaster")
data class ItemUnitMasterEntity(
    @PrimaryKey @ColumnInfo(name = "item_unit_id") val itemUnitId: Int,
    @ColumnInfo(name = "item_unit_code") val itemUnitCode: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)


@Entity(
    tableName = "ItemTypeMaster", foreignKeys = [
        ForeignKey(
            entity = ItemUnitMasterEntity::class,
            parentColumns = ["item_unit_id"],
            childColumns = ["item_unit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ItemTypeMasterEntity(
    @PrimaryKey @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "item_type_code") val itemTypeCode: String,
    @ColumnInfo(name = "item_type_name") val itemTypeName: String,
    @ColumnInfo(name = "item_unit_id") val itemUnitId: Int,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(
    tableName = "ItemTypeFieldSettingMaster",
    primaryKeys = ["item_type_id", "field_id"],
)
data class ItemTypeFieldSettingMasterEntity(
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "field_id") val fieldId: Int,
    @ColumnInfo(name = "is_required") val isRequired: Boolean,
    @ColumnInfo(name = "is_visible") val isVisible: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)

@Entity(tableName = "OutBoundSession")
data class OutBoundSessionEntity(
    @PrimaryKey @ColumnInfo(name = "out_bound_session_id") val outBoundSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)

@Entity(tableName = "InBoundSession")
data class InBoundSessionEntity(
    @PrimaryKey @ColumnInfo(name = "in_bound_session_id") val inBoundSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)

//@Entity(
//    tableName = "FieldMaster",
//)
//data class FieldMasterEntity(
//    @PrimaryKey @ColumnInfo(name = "field_id") val fieldId: Int,
//    @PrimaryKey @ColumnInfo(name = "field_name") val fieldName: String,
//    @PrimaryKey @ColumnInfo(name = "data_type") val dataType: String,
//    @PrimaryKey @ColumnInfo(name = "max_length") val maxLength: Int,
//    @PrimaryKey @ColumnInfo(name = "min_value") val minValue: Int,
//    @PrimaryKey @ColumnInfo(name = "max_value") val maxValue: Int,
//    @PrimaryKey @ColumnInfo(name = "regex_pattern") val regexPattern: String,
//    @PrimaryKey @ColumnInfo(name = "control_type") val ControlType: String,
//    @ColumnInfo(name = "created_at") val createdAt: String,
//    @ColumnInfo(name = "updated_at") val updatedAt: String,
//)

@Entity(
    tableName = "OutBoundEvent",
    foreignKeys = [
        ForeignKey(
            entity = OutBoundSessionEntity::class,
            parentColumns = ["out_bound_session_id"],
            childColumns = ["out_bound_session_id"],
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
            entity = EventTypeMasterEntity::class,
            parentColumns = ["id"],
            childColumns = ["process_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class OutBoundEventEntity(
    @PrimaryKey @ColumnInfo(name = "outbound_event_id") val outBoundEventId: Int,
    @ColumnInfo(name = "outbound_session_id") val outBoundSessionId: Int,
    @ColumnInfo(name = "ledger_item_id") val ledgerItemId: Int,
    @ColumnInfo(name = "process_type_id") val processTypeId: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "occurred_at") val occurredAt: String,
    @ColumnInfo(name = "registered_at") val registeredAt: String,
)

@Entity(
    tableName = "InBoundEvent",
    foreignKeys = [
        ForeignKey(
            entity = InBoundSessionEntity::class,
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
data class InBoundEventEntity(
    @PrimaryKey @ColumnInfo(name = "inbound_event_id") val inBoundEventId: Int,
    @ColumnInfo(name = "inbound_session_id") val inBoundSessionId: Int,
    @ColumnInfo(name = "item_type_id") val itemTypeId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "grade") val grade: String,
    @ColumnInfo(name = "specific_gravity") val specificGravity: Int,
    @ColumnInfo(name = "thickness") val thickness: Int,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "length") val length: Int,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "winder_info") val winderInfo: String,
    @ColumnInfo(name = "misroll_reason") val misrollReason: String,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "occurred_at") val occurredAt: String,
    @ColumnInfo(name = "registered_at") val registeredAt: String,
)




