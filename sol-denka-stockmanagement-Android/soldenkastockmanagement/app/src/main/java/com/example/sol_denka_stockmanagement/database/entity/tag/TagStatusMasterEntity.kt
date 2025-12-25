package com.example.sol_denka_stockmanagement.database.entity.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TagStatusMaster")
data class TagStatusMasterEntity(
    @PrimaryKey @ColumnInfo(name = "tag_status_id") val tagStatusId: Int,
    @ColumnInfo(name = "status_code") val statusCode: String,
    @ColumnInfo(name = "status_name") val statusName: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)