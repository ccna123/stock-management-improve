package com.example.sol_denka_stockmanagement.database.entity.inventory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity

@Entity(tableName = "InventorySession", foreignKeys = [
    ForeignKey(
        entity = LocationMasterEntity::class,
        parentColumns = ["location_id"],
        childColumns = ["location_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )
])
data class InventorySessionEntity(
    @PrimaryKey @ColumnInfo(name = "inventory_session_id") val inventorySessionId: Int,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,

)
