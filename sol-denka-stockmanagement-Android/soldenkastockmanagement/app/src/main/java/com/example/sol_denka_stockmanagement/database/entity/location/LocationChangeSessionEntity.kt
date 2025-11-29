package com.example.sol_denka_stockmanagement.database.entity.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationChangeSession")
data class LocationChangeSessionEntity(
    @PrimaryKey @ColumnInfo(name = "location_change_session_id") val locationChangeSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)
