package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeSessionEntity
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeSessionDao {

    @Query("SELECT * FROM LocationChangeSession")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeSessionEntity>>

    @Query("SELECT location_change_session_id AS sessionId, executed_at AS timeStamp FROM LocationChangeSession")
    suspend fun getSession(): List<com.example.sol_denka_stockmanagement.domain.model.session.SessionModel>

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeSessionEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeSessionEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.location.LocationChangeSessionEntity)
}