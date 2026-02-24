package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import com.example.sol_denka_stockmanagement.model.outbound.OutboundEventForExportModel
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundEventDao {

    @Query("SELECT * FROM OutboundEvent")
    fun get(): Flow<List<OutBoundEventEntity>>

    @Query("""
        SELECT event.ledger_item_id AS ledgerItemId,
        event.tag_id AS tagId,
        event.process_type_id AS processTypeId,
        session.device_id AS deviceId,
        event.source_event_id AS sourceEventId,
        event.memo AS memo,
        event.processed_at AS processedAt,
        event.registered_at AS registeredAt
        FROM OutboundEvent AS event
        LEFT JOIN OutboundSession AS session ON session.outbound_session_id = event.outbound_session_id
        WHERE session.outbound_session_id = :sessionId
    """)
    suspend fun getEventBySessionId(sessionId: Int): List<OutboundEventForExportModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: OutBoundEventEntity): Long

    @Update
    suspend fun update(e: OutBoundEventEntity)

    @Delete
    suspend fun delete(e: OutBoundEventEntity)
}