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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "location_code") val locationCode: String,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
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



