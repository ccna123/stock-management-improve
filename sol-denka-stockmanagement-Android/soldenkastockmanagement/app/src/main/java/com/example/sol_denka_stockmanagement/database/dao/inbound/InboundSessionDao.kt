package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import com.example.sol_denka_stockmanagement.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundSessionDao {

    @Query("SELECT * FROM InboundSession")
    fun get(): Flow<List<InboundSessionEntity>>

    @Query("SELECT inbound_session_id AS sessionId, executed_at AS timeStamp FROM InboundSession")
    suspend fun getExecutedAt(): List<SessionModel>

    @Insert
    suspend fun insert(e: InboundSessionEntity): Long

    @Update
    suspend fun update(e: InboundSessionEntity)

    @Delete
    suspend fun delete(e: InboundSessionEntity)
}