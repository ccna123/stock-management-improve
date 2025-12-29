package com.example.sol_denka_stockmanagement.database.dao.csv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvHistoryDao {

    @Query("SELECT * FROM CsvHistory")
    fun get(): Flow<List<CsvHistoryEntity>>

    @Insert
    suspend fun insert(e: CsvHistoryEntity): Long

    @Update
    suspend fun update(e: CsvHistoryEntity)

    @Delete
    suspend fun delete(e: CsvHistoryEntity)
}