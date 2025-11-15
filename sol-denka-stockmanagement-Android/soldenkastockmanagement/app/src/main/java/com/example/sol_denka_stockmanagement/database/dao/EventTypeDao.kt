package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.EventTypeMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventTypeDao: IDao<EventTypeMasterEntity> {

    @Query("SELECT * FROM EventTypeMaster")
    override fun get(): Flow<List<EventTypeMasterEntity>>

    @Insert
    override suspend fun insert(e: EventTypeMasterEntity)

    @Update
    override suspend fun update(e: EventTypeMasterEntity)

    @Delete
    override suspend fun delete(e: EventTypeMasterEntity)
}