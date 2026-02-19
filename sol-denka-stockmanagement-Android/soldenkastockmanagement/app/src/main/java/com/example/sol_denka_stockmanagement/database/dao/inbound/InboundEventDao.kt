package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundEventDao {

    @Query("SELECT * FROM InBoundEvent")
    fun get(): Flow<List<InboundEventEntity>>

    @Query("SELECT * FROM inboundevent WHERE inbound_session_id = :sessionId")
    suspend fun getEventBySessionId(sessionId: Int): InboundEventEntity?

    @Insert
    suspend fun insert(e: InboundEventEntity): Long

    @Update
    suspend fun update(e: InboundEventEntity)

    @Delete
    suspend fun delete(e: InboundEventEntity)
}