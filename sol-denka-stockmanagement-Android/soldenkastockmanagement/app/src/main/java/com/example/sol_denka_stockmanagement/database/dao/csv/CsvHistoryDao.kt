package com.example.sol_denka_stockmanagement.database.dao.csv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvHistoryDao {

    @Query("SELECT * FROM CsvHistory")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity>>

    @Query("SELECT COUNT(*) FROM CsvHistory")
    suspend fun countRecord(): Int

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvHistoryEntity)
}