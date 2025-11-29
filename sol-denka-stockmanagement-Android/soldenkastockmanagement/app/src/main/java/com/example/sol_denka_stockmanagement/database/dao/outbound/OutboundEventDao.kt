package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundEventDao: IDao<OutBoundEventEntity> {

    @Query("SELECT * FROM OutboundEvent")
    override fun get(): Flow<List<OutBoundEventEntity>>

    @Insert
    override suspend fun insert(e: OutBoundEventEntity)

    @Update
    override suspend fun update(e: OutBoundEventEntity)

    @Delete
    override suspend fun delete(e: OutBoundEventEntity)
}