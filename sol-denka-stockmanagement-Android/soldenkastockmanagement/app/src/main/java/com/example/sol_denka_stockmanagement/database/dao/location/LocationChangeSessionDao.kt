package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeSessionDao: IDao<LocationChangeSessionEntity> {

    @Query("SELECT * FROM LocationChangeSession")
    override fun get(): Flow<List<LocationChangeSessionEntity>>

    @Insert
    override suspend fun insert(e: LocationChangeSessionEntity): Long

    @Update
    override suspend fun update(e: LocationChangeSessionEntity)

    @Delete
    override suspend fun delete(e: LocationChangeSessionEntity)
}