package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.CsvHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvHistoryDao: IDao<CsvHistoryEntity> {

    @Query("SELECT * FROM CsvHistory")
    override fun get(): Flow<List<CsvHistoryEntity>>

    @Insert
    override suspend fun insert(e: CsvHistoryEntity)

    @Update
    override suspend fun update(e: CsvHistoryEntity)

    @Delete
    override suspend fun delete(e: CsvHistoryEntity)
}