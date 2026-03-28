package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundSessionDao {

    @Query("SELECT * FROM Outboundsession")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity>>

    @Query("SELECT outbound_session_id AS sessionId, executed_at AS timeStamp FROM OutboundSession")
    suspend fun getSession(): List<com.example.sol_denka_stockmanagement.domain.model.session.SessionModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.outbound.OutboundSessionEntity)
}