package com.example.sol_denka_stockmanagement.database.entity.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity

@Entity(
    tableName = "TagMaster", foreignKeys = [
        ForeignKey(
            entity = TagStatusMasterEntity::class,
            parentColumns = ["tag_status_id"],
            childColumns = ["tag_status_id"],
        )
    ],
    indices = [
        Index(value = ["tag_status_id"]),
    ]
)
data class TagMasterEntity(
    @PrimaryKey @ColumnInfo(name = "tag_id") val tagId: Int,
    @ColumnInfo(name = "tag_status_id") val tagStatusId: Int,
    @ColumnInfo(name = "epc") val epc: String,
)
