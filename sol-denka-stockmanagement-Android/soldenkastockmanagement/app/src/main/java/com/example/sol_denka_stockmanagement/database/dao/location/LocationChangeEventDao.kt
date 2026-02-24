package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventForExportModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeEventDao {

    @Query("SELECT * FROM LocationChangeEvent")
    fun get(): Flow<List<LocationChangeEventEntity>>

    @Query("""
        SELECT detail.source_event_id AS sourceEventId,
        detail.location_id AS locationId,
        detail.ledger_item_id AS ledgerItemId,
        detail.memo AS memo,
        detail.scanned_at AS scannedAt,
        session.device_id AS deviceId,
        session.executed_at AS executedAt
        FROM LocationChangeEvent AS detail
        LEFT JOIN LocationChangeSession AS session ON session.location_change_session_id = detail.location_change_session_id
        WHERE session.location_change_session_id = :sessionId
    """)
    suspend fun getEventBySessionId(sessionId: Int): List<LocationChangeEventForExportModel>

    @Insert
    suspend fun insert(e: LocationChangeEventEntity): Long

    @Update
    suspend fun update(e: LocationChangeEventEntity)

    @Delete
    suspend fun delete(e: LocationChangeEventEntity)
}