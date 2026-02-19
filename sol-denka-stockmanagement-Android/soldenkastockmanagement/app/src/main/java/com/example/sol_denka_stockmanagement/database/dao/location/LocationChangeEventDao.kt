package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeEventDao {

    @Query("SELECT * FROM LocationChangeEvent")
    fun get(): Flow<List<LocationChangeEventEntity>>

    @Query("SELECT * FROM locationchangeevent WHERE location_change_session_id = :sessionId")
    suspend fun getEventBySessionId(sessionId: Int): LocationChangeEventEntity

    @Insert
    suspend fun insert(e: LocationChangeEventEntity): Long

    @Update
    suspend fun update(e: LocationChangeEventEntity)

    @Delete
    suspend fun delete(e: LocationChangeEventEntity)
}