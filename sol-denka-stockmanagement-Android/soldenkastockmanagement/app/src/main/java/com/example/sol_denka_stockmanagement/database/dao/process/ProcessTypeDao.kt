package com.example.sol_denka_stockmanagement.database.dao.process

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcessTypeDao {

    @Query("SELECT * FROM ProcessType")
    fun get(): Flow<List<ProcessTypeEntity>>

    @Query("SELECT process_type_id FROM ProcessType WHERE process_name LIKE '%' || :processTypeName || '%'")
    suspend fun getIdByName(processTypeName: String): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: ProcessTypeEntity): Long

    @Update
    suspend fun update(e: ProcessTypeEntity)

    @Delete
    suspend fun delete(e: ProcessTypeEntity)
}