package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeSessionDao {

    @Query("SELECT * FROM LocationChangeSession")
    fun get(): Flow<List<LocationChangeSessionEntity>>

    @Query("SELECT executed_at FROM LocationChangeSession")
    suspend fun getExecutedAt(): List<String>

    @Insert
    suspend fun insert(e: LocationChangeSessionEntity): Long

    @Update
    suspend fun update(e: LocationChangeSessionEntity)

    @Delete
    suspend fun delete(e: LocationChangeSessionEntity)
}