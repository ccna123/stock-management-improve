package com.example.sol_denka_stockmanagement.database.entity.process

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.constant.HandlingMethod

@Entity(tableName = "ProcessTypeEntity")
data class ProcessTypeEntity(
    @PrimaryKey @ColumnInfo(name = "process_type_id") val processTypeId: Int,
    @ColumnInfo(name = "process_code") val processCode: HandlingMethod,
    @ColumnInfo(name = "process_name") val processName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
