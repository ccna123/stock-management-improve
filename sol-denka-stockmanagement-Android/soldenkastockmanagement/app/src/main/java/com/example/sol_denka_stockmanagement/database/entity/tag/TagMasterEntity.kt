package com.example.sol_denka_stockmanagement.database.entity.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TagMaster")
data class TagMasterEntity(
    @PrimaryKey @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "epc") val epc: String,
)
