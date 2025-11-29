package com.example.sol_denka_stockmanagement.database.dao.inbound

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboundSessionDao: IDao<InboundSessionEntity> {

    @Query("SELECT * FROM InboundSession")
    override fun get(): Flow<List<InboundSessionEntity>>

    @Insert
    override suspend fun insert(e: InboundSessionEntity)

    @Update
    override suspend fun update(e: InboundSessionEntity)

    @Delete
    override suspend fun delete(e: InboundSessionEntity)
}