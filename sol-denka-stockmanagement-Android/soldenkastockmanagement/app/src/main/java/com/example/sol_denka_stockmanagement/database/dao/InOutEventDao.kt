package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.InOutEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InOutEventDao: IDao<InOutEventEntity> {

    @Query("SELECT * FROM InOutEvent")
    override fun get(): Flow<List<InOutEventEntity>>

    @Insert
    override suspend fun insert(e: InOutEventEntity)

    @Update
    override suspend fun update(e: InOutEventEntity)

    @Delete
    override suspend fun delete(e: InOutEventEntity)
}