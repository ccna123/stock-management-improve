package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import com.example.sol_denka_stockmanagement.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeSessionDao {

    @Query("SELECT * FROM LocationChangeSession")
    fun get(): Flow<List<LocationChangeSessionEntity>>

    @Query("SELECT location_change_session_id AS sessionId, executed_at AS timeStamp FROM LocationChangeSession")
    suspend fun getExecutedAt(): List<SessionModel>

    @Insert
    suspend fun insert(e: LocationChangeSessionEntity): Long

    @Update
    suspend fun update(e: LocationChangeSessionEntity)

    @Delete
    suspend fun delete(e: LocationChangeSessionEntity)
}