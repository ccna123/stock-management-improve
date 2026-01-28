package com.example.sol_denka_stockmanagement.database.entity.winder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Winder")
data class WinderEntity(
    @PrimaryKey@ColumnInfo(name = "winder_id") val winderId: Int,
    @ColumnInfo(name = "winder_name") val winderName: String,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
)
