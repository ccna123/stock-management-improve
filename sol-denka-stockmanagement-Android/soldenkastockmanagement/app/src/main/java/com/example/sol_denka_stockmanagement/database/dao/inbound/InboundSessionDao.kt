package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundSessionDao {

    @Query("SELECT * FROM InboundSession")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity>>

    @Query("SELECT inbound_session_id AS sessionId, executed_at AS timeStamp FROM InboundSession")
    suspend fun getSession(): List<com.example.sol_denka_stockmanagement.domain.model.session.SessionModel>

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.inbound.InboundSessionEntity)
}