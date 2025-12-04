package com.example.sol_denka_stockmanagement.database.dao.outbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundSessionDao: IDao<OutboundSessionEntity> {

    @Query("SELECT * FROM Outboundsession")
    override fun get(): Flow<List<OutboundSessionEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: OutboundSessionEntity): Long

    @Update
    override suspend fun update(e: OutboundSessionEntity)

    @Delete
    override suspend fun delete(e: OutboundSessionEntity)
}