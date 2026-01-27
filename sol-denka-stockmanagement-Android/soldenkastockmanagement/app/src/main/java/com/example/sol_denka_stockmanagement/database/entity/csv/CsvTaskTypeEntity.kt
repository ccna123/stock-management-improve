package com.example.sol_denka_stockmanagement.database.entity.csv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CsvTaskType")
data class CsvTaskTypeEntity(
    @PrimaryKey @ColumnInfo(name = "csv_task_type_id") val csvTaskTypeId: Int = 0,
    @ColumnInfo(name = "csv_task_code") val csvTaskCode: String,
    @ColumnInfo(name = "csv_task_name") val csvTaskName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)