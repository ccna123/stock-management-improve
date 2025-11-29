package com.example.sol_denka_stockmanagement.database.dao.process

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcessTypeDao: IDao<ProcessTypeEntity> {

    @Query("SELECT * FROM ProcessType")
    override fun get(): Flow<List<ProcessTypeEntity>>

    @Insert
    override suspend fun insert(e: ProcessTypeEntity)

    @Update
    override suspend fun update(e: ProcessTypeEntity)

    @Delete
    override suspend fun delete(e: ProcessTypeEntity)
}