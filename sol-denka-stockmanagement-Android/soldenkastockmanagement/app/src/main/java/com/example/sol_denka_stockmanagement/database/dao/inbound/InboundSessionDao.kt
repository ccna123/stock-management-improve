package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundSessionDao {

    @Query("SELECT * FROM InboundSession")
    fun get(): Flow<List<InboundSessionEntity>>

    @Insert
    suspend fun insert(e: InboundSessionEntity): Long

    @Update
    suspend fun update(e: InboundSessionEntity)

    @Delete
    suspend fun delete(e: InboundSessionEntity)
}