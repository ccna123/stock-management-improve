package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryDetailEntity
import com.example.sol_denka_stockmanagement.model.inventory.InventoryEventForExportModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDetailDao {

    @Query("SELECT * FROM InventoryDetail")
    fun get(): Flow<List<InventoryDetailEntity>>

    @Query("""
        SELECT session.source_session_uuid AS sourceSessionId,
        session.location_id AS locationId,
        session.device_id AS deviceId,
        detail.ledger_item_id AS ledgerItemId,
        detail.tag_id AS tagId,
        session.memo AS memo,
        detail.scanned_at AS scannedAt,
        session.executed_at AS executedAt
        FROM InventoryDetail AS detail
        LEFT JOIN InventorySession AS session ON session.inventory_session_id = detail.inventory_session_id
        WHERE session.inventory_session_id = :sessionId
    """)
    suspend fun getEventBySessionId(sessionId: Int): List<InventoryEventForExportModel>
    @Insert
    suspend fun insert(e: InventoryDetailEntity): Long

    @Update
    suspend fun update(e: InventoryDetailEntity)

    @Delete
    suspend fun delete(e: InventoryDetailEntity)
}