package com.example.sol_denka_stockmanagement.database.entity.csv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult

@Entity(
    tableName = "CsvHistory", foreignKeys = [
        ForeignKey(
            entity = CsvTaskTypeEntity::class,
            parentColumns = ["csv_task_type_id"],
            childColumns = ["csv_task_type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["csv_task_type_id"])
    ]
)
data class CsvHistoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "csv_history_id") val csvHistoryId: Int = 0,
    @ColumnInfo(name = "csv_task_type_id") val csvTaskTypeId: Int,
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "direction") val direction: CsvHistoryDirection,
    @ColumnInfo(name = "result") val result: CsvHistoryResult,
    @ColumnInfo(name = "record_num") val recordNum: Int?,
    @ColumnInfo(name = "error_message") val errorMessage: String?,
    @ColumnInfo(name = "executed_at") val executedAt: String,
)
