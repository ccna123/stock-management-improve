package com.example.sol_denka_stockmanagement.database.entity.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationMaster")
data class LocationMasterEntity(
    @PrimaryKey @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "location_code") val locationCode: String?,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "memo") val memo: String?,
)
