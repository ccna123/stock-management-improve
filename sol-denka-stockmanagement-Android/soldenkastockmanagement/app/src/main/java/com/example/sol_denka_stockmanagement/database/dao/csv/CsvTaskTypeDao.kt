package com.example.sol_denka_stockmanagement.database.dao.csv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvTaskTypeDao {

    @Query("SELECT * FROM CsvTaskType")
    fun get(): Flow<List<CsvTaskTypeEntity>>

    @Query("SELECT csv_task_type_id FROM CsvTaskType WHERE csv_task_code = :taskCode")
    suspend fun getIdByTaskCode(taskCode: String): Int

    @Insert
    suspend fun insert(e: CsvTaskTypeEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<CsvTaskTypeEntity>)

    @Update
    suspend fun update(e: CsvTaskTypeEntity)

    @Delete
    suspend fun delete(e: CsvTaskTypeEntity)

    @Query("DELETE FROM ItemTypeMaster")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(e: List<CsvTaskTypeEntity>) {
        deleteAll()
        insertAll(e)
    }
}