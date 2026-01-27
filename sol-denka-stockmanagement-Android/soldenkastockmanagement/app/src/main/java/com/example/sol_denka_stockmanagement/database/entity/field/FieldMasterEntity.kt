package com.example.sol_denka_stockmanagement.database.entity.field

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FieldMaster")
data class FieldMasterEntity(
    @PrimaryKey@ColumnInfo(name = "field_id") val fieldId: Int,
    @ColumnInfo(name = "field_name") val fieldName: String,
    @ColumnInfo(name = "field_code") val fieldCode: String,
    @ColumnInfo(name = "data_type") val dataType: String,
    @ColumnInfo(name = "control_type") val controlType: String,
)
