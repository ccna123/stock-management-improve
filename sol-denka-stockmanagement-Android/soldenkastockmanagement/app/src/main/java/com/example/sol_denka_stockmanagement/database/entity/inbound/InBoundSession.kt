package com.example.sol_denka_stockmanagement.database.entity.inbound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InboundSession")
data class InboundSessionEntity(
    @PrimaryKey @ColumnInfo(name = "inbound_session_id") val inboundSessionId: Int,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)
