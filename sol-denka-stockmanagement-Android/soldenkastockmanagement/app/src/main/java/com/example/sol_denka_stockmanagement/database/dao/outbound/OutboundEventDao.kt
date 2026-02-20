package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundEventDao {

    @Query("SELECT * FROM OutboundEvent")
    fun get(): Flow<List<OutBoundEventEntity>>

    @Query("SELECT * FROM outboundevent WHERE outbound_session_id = :sessionId")
    suspend fun getEventBySessionId(sessionId: Int): List<OutBoundEventEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: OutBoundEventEntity): Long

    @Update
    suspend fun update(e: OutBoundEventEntity)

    @Delete
    suspend fun delete(e: OutBoundEventEntity)
}