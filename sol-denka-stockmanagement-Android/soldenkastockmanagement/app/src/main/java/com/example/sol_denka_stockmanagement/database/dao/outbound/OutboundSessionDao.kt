package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundSessionDao {

    @Query("SELECT * FROM Outboundsession")
    fun get(): Flow<List<OutboundSessionEntity>>

    @Query("SELECT outbound_session_id AS sessionId, executed_at AS timeStamp FROM OutboundSession")
    suspend fun getSession(): List<SessionModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: OutboundSessionEntity): Long

    @Update
    suspend fun update(e: OutboundSessionEntity)

    @Delete
    suspend fun delete(e: OutboundSessionEntity)
}