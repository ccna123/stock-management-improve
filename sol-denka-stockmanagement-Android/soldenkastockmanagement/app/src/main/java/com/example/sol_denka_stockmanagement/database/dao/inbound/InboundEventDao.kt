package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundEventDao {

    @Query("SELECT * FROM InBoundEvent")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity>>

    @Query("SELECT * FROM inboundevent WHERE inbound_session_id = :sessionId")
    suspend fun getEventBySessionId(sessionId: Int): com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity?

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundEventEntity)
}