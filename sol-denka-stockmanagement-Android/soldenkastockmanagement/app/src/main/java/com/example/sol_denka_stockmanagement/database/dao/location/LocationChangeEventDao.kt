package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationChangeEventDao: IDao<LocationChangeEventEntity> {

    @Query("SELECT * FROM LocationChangeEvent")
    override fun get(): Flow<List<LocationChangeEventEntity>>

    @Insert
    override suspend fun insert(e: LocationChangeEventEntity)

    @Update
    override suspend fun update(e: LocationChangeEventEntity)

    @Delete
    override suspend fun delete(e: LocationChangeEventEntity)
}