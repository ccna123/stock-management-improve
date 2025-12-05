package com.example.sol_denka_stockmanagement.database.dao.csv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsvTaskTypeDao: IDao<CsvTaskTypeEntity> {

    @Query("SELECT * FROM CsvTaskType")
    override fun get(): Flow<List<CsvTaskTypeEntity>>

    @Query("SELECT csv_task_type_id FROM CsvTaskType WHERE csv_task_code = :taskCode")
    suspend fun getIdByTaskCode(taskCode: String): Int

    @Insert
    override suspend fun insert(e: CsvTaskTypeEntity): Long

    @Update
    override suspend fun update(e: CsvTaskTypeEntity)

    @Delete
    override suspend fun delete(e: CsvTaskTypeEntity)
}