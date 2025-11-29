package com.example.sol_denka_stockmanagement.model.csv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CsvTaskType")
data class CsvTaskTypeModel(
    @PrimaryKey @ColumnInfo(name = "csv_task_type_id") val csvTaskTypeId: Int,
    @ColumnInfo(name = "csv_task_code") val csvTaskCode: String,
    @ColumnInfo(name = "csv_task_name") val csvTaskName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,

)
