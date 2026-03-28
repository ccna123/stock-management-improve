package com.example.sol_denka_stockmanagement.database.dao.csv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvTaskTypeDao {

    @Query("SELECT * FROM CsvTaskType")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity>>

    @Query("SELECT COUNT(*) FROM CsvTaskType")
    suspend fun countRecord(): Int

    @Query("SELECT csv_task_type_id FROM CsvTaskType WHERE csv_task_code = :taskCode")
    suspend fun getIdByTaskCode(taskCode: String): Int

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity>)

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity)

    @Query("DELETE FROM ItemTypeMaster")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.csv.CsvTaskTypeEntity>)
}