package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundEventDao: IDao<InboundEventEntity> {

    @Query("SELECT * FROM InBoundEvent")
    override fun get(): Flow<List<InboundEventEntity>>

    @Insert
    override suspend fun insert(e: InboundEventEntity): Long

    @Update
    override suspend fun update(e: InboundEventEntity)

    @Delete
    override suspend fun delete(e: InboundEventEntity)
}