package com.example.sol_denka_stockmanagement.database.entity.outbound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OutboundSession")
data class OutboundSessionEntity(
    @PrimaryKey @ColumnInfo(name = "out_bound_session_id") val outboundSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)
